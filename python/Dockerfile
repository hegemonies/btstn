FROM python:3.11-slim-buster

WORKDIR /app

COPY ./ /app

# Install poetry
RUN apt update && \
        apt install curl gcc -y && \
        curl -sSL https://install.python-poetry.org | python - && \
        apt clean

# Install project
RUN /root/.local/bin/poetry install

CMD /root/.local/bin/poetry run python3 /app/telegram-scrapper.py
