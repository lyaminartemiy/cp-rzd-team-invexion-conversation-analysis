# Use the Alpine Linux base image
FROM python:3.11-alpine

# Set environment variables
ENV PYTHONDONTWRITEBYTECODE 1
ENV PYTHONUNBUFFERED 1

# Install system dependencies
RUN apk update \
    && apk add --no-cache \
        build-base \
        libffi-dev \
        openssl-dev \
        bash \
    && rm -rf /var/cache/apk/*

# Set the working directory in the container
WORKDIR /app

# Copy the requirements file into the container
COPY requirements.txt .

# Install Python dependencies
RUN pip install --no-cache-dir -r requirements.txt

# Copy the rest of the application code into the container
COPY . .

EXPOSE 8000

# Specify the command to run on container start
CMD [ "python", "main.py" ]