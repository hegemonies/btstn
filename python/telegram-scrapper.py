from telethon.sync import TelegramClient, events
from dotenv import dotenv_values
import requests
import schedule, time
import asyncio
import json
import logging

def start_telegram_channel_scrapper():
    try:
        with client:
            client.loop.run_until_complete(scrap_telegram_channels())
    except Exception as error:
        logging.error(f"Error executing of telegram channels scrapping: {error.message}")


async def scrap_telegram_channels():
    logging.info("Start telegram scrapper")

    # get all dialogs and filter them
    dialogs = []

    logging.info("Found such dialogs:")
    
    async for dialog in client.iter_dialogs():
        logging.info('{:>14}: {}'.format(dialog.id, dialog.title))
        
        if dialog.title in telegram_channel_names:
            dialogs.append(dialog)
    
    logging.info("Dialogs after the filter:")

    for dialog in dialogs:
        logging.info("{:>14}: {}".format(dialog.id, dialog.title))

    for dialog in dialogs:
        logging.info(f"Scrapping of channel {dialog.title}")
        # get messages from channels
        async for message in client.iter_messages(dialog, limit=message_per_channel_number, wait_time=500):
            # send messages to btstn database api
            try:
                requests.post(telegram_grabber_database_url, data=json.dumps({"channelName": dialog.title,"messageId": message.id, "messageContent": message.text}))
            except Exception as error:
                logging.error(f"Error send message from telegram channel to btstn api {error.message}")

    logging.info("Finish telegram scrapper")


logging.basicConfig(format='%(asctime)s - %(levelname)s - %(message)s', datefmt='%m/%d/%Y %I:%M:%S %p', level=logging.INFO)

envs = dotenv_values(".env")

telegram_client_name = "btstn-telegram-scrapper-python"
telegram_client_api_id = envs["API_ID"]
telegram_client_api_hash = envs["API_HASH"]
telegram_channel_names = envs["TELEGRAM_CHANNEL_NAMES"].split(";")
telegram_grabber_database_url = envs["TELEGRAM_GRABBER_DATABASE_URL"]
message_per_channel_number = int(envs["MESSAGE_PER_CHANNEL_NUMBER"])

client = TelegramClient(telegram_client_name, telegram_client_api_id, telegram_client_api_hash)

schedule.every().hour.do(start_telegram_channel_scrapper)

while True:
    schedule.run_all()
    time.sleep(1)
