from telethon.sync import TelegramClient
from dotenv import dotenv_values
import requests
import schedule, time
import asyncio
import json
import logging
import os

def start_telegram_channel_scrapper():
    try:
        with client:
            client.loop.run_until_complete(scrap_telegram_channels())
    except Exception as error:
        logging.error(f"Error executing of telegram channels scrapping: {error.message}")
        exit(1)


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
        async for message in client.iter_messages(dialog, limit=message_per_channel_number, wait_time=5):
            logging.info(f"Send message {message.id} from {dialog.title} to database api")
            # send messages to btstn database api
            try:
                requests.post(
                    telegram_grabber_database_url, 
                    data=json.dumps(
                        {
                            "channelName": dialog.title,
                            "messageId": message.id,
                            "messageContent": message.text,
                            "date": message.date.timestamp()
                        },
                        default=str
                    ), 
                    headers={"Content-Type":"application/json"},
                    timeout=5
                )
            except Exception as error:
                logging.error(f"Error send message from telegram channel to btstn api {error.message}")
                exit(1)

    logging.info("Finish telegram scrapper")


logging.basicConfig(format='%(asctime)s - %(levelname)s - %(message)s', datefmt='%m/%d/%Y %I:%M:%S %p', level=logging.INFO)

envs = dotenv_values(".env")


telegram_client_name = "btstn-telegram-scrapper-python"
print(f"telegram_client_name={telegram_client_name}")

telegram_client_api_id = envs["API_ID"] if envs["API_ID"] != "" else os.environ["API_ID"]
print(f"telegram_client_api_id={telegram_client_api_id}")

telegram_client_api_hash = envs["API_HASH"] if envs["API_HASH"] != "" else os.environ["API_HASH"]
print(f"telegram_client_api_hash={telegram_client_api_hash}")

telegram_channel_names = envs["TELEGRAM_CHANNEL_NAMES"].split(";") if envs["TELEGRAM_CHANNEL_NAMES"] != "" else os.environ["TELEGRAM_CHANNEL_NAMES"].split(";")
print(f"telegram_channel_names={telegram_channel_names}")

telegram_grabber_database_url = envs["TELEGRAM_GRABBER_DATABASE_URL"] if envs["TELEGRAM_GRABBER_DATABASE_URL"] != "" else os.environ["TELEGRAM_GRABBER_DATABASE_URL"]
print(f"telegram_grabber_database_url={telegram_grabber_database_url}")

message_per_channel_number = int(envs["MESSAGE_PER_CHANNEL_NUMBER"]) if envs["MESSAGE_PER_CHANNEL_NUMBER"] != "" else int(os.environ["MESSAGE_PER_CHANNEL_NUMBER"])
print(f"message_per_channel_number={message_per_channel_number}")

client = TelegramClient(telegram_client_name, telegram_client_api_id, telegram_client_api_hash)

schedule.every(60).minutes.do(start_telegram_channel_scrapper)

while True:
    schedule.run_all()
    schedule.run_pending()
    time.sleep(1)
