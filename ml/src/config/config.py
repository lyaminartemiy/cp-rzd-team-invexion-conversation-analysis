import os
from dotenv import load_dotenv


class Config:
    """
    Configuration Class.

    This class sets up environment variables upon instantiation.
    """

    def __init__(self):
        """
        Initialize configuration.

        Sets up environment variables upon instantiation.

        Parameters
        ----------
        None

        Returns
        -------
        None
        """
        self.SPEECH2TEXT_URL = None
        self.SPEECH2TEXT_TOKEN = None
        self.LLM_TOKEN = None
        self.S3_ENDPOINT = None
        self.S3_ACCESS_KEY = None
        self.S3_SECRET_KEY = None

        self.init_env()

    def init_env(self):
        """
        Initialize environment variables.

        Loads environment variables from a .env file and sets up proxy and LLM token.

        Parameters
        ----------
        None

        Returns
        -------
        None
        """
        load_dotenv()

        # HuggingFace setup
        self.SPEECH2TEXT_URL = os.getenv("SPEECH2TEXT_URL", "")
        self.SPEECH2TEXT_TOKEN = os.getenv("SPEECH2TEXT_TOKEN", "")

        # LLM token setup
        self.LLM_TOKEN = os.getenv("LLM_TOKEN", "")

        # S3 setup
        self.S3_ENDPOINT = os.getenv("S3_ENDPOINT", "")
        self.S3_ACCESS_KEY = os.getenv("S3_ACCESS_KEY", "")
        self.S3_SECRET_KEY = os.getenv("S3_SECRET_KEY", "")


config = Config()
