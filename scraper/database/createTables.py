import psycopg2
from config import config

def create_tables():
    """ create tables in the PostgreSQL database"""


    commands = (
        """
        CREATE TABLE beers (
            beer_id SERIAL PRIMARY KEY,
            beer_name VARCHAR(255) NOT NULL,
            beer_link VARCHAR(255),
            beer_alcohol FLOAT,
            beer_volume INTEGER,
            beer_taste VARCHAR(255),
            beer_stars FLOAT,
            beer_price INTEGER
        )
        """,
        """ CREATE TABLE users (
                user_id SERIAL PRIMARY KEY,
                username VARCHAR(255) NOT NULL,
                password VARCHAR(255) NOT NULL,
                enabled BOOLEAN
                )
        """,
        """
        CREATE TABLE role (
            role_id SERIAL PRIMARY KEY,
            role VARCHAR(255)
        )
        """,
        """
        CREATE TABLE user_role (
            username INTEGER NOT NULL,
            rolename INTEGER NOT NULL,
            PRIMARY KEY (user_id , role_id),
            FOREIGN KEY (user_id)
                REFERENCES users (user_id)
                ON UPDATE CASCADE ON DELETE CASCADE,
            FOREIGN KEY (role_id)
                REFERENCES role (role_id)
                ON UPDATE CASCADE ON DELETE CASCADE
        )
        """,

        """
        CREATE TABLE favorite_beers (
                user_id INTEGER NOT NULL,
                beer_id INTEGER NOT NULL,
                PRIMARY KEY (user_id , beer_id),
                FOREIGN KEY (user_id)
                    REFERENCES users (user_id)
                    ON UPDATE CASCADE ON DELETE CASCADE,
                FOREIGN KEY (beer_id)
                    REFERENCES beers (beer_id)
                    ON UPDATE CASCADE ON DELETE CASCADE
        )
        """,
        """
        CREATE TABLE want_to_try (
                beer_id INTEGER NOT NULL,
                user_id INTEGER NOT NULL,
                PRIMARY KEY (user_id , beer_id),
                FOREIGN KEY (user_id)
                    REFERENCES users (user_id)
                    ON UPDATE CASCADE ON DELETE CASCADE,
                FOREIGN KEY (beer_id)
                    REFERENCES beers (beer_id)
                    ON UPDATE CASCADE ON DELETE CASCADE
        )
        """)
    conn = None
    try:
        params = config()
        conn = psycopg2.connect(**params)
        cur = conn.cursor()
        for command in commands:
            print("ass")
            cur.execute(command)
        cur.close()
        conn.commit()
    except (Exception, psycopg2.DatabaseError) as err:
        print(err)
    finally:
        if conn is not None:
            conn.close()

create_tables()
