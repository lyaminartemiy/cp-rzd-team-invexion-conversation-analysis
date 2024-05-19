from minio import Minio

from src.config.config import config


async def init_client() -> Minio:
    """
    Initializes a Minio client with provided configuration.

    Returns:
    --------
    Minio
        Initialized Minio client.
    """
    client = Minio(
        config.S3_ENDPOINT,
        access_key=config.S3_ACCESS_KEY,
        secret_key=config.S3_SECRET_KEY,
        secure=False,
    )
    return client


async def get_file_from_s3(uuid: str) -> bytes:
    """
    Retrieves a file from an S3 bucket.

    Parameters:
    -----------
    uuid : str
        The unique identifier of the file in the S3 bucket.

    Returns:
    --------
    bytes
        The contents of the file.
    """
    response = None
    client = await init_client()
    try:
        response = client.get_object("test", uuid)
        return response.data
    finally:
        if response:
            response.close()
            response.release_conn()
