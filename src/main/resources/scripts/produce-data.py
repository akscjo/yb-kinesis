import boto3
import json
import time
from datetime import datetime, timezone
import random
from faker import Faker
import uuid
import signal
import sys

# Initialize a Kinesis client with AWS
# boto3.setup_default_session(profile_name='amit-us-west-2-profile')

kinesis = boto3.client('kinesis', region_name='us-east-1')
fake = Faker()

# Pre-defined categories and state codes
categories = [
    'Electronics', 'Apparel', 'Home', 'Grocery', 
    'Outdoor', 'Beauty & Health', 'Toys', 'Sports', 
    'Automotive', 'Books', 'Music', 'Office Supplies', 
    'Pet Supplies', 'Garden', 'Furniture', 'Footwear',
    'Jewelry', 'Watches', 'Tools & Home Improvement', 
    'Video Games', 'Art & Craft', 'Party Supplies', 
    'School Supplies', 'Kitchen & Dining'
]

states = [
    'AL', 'AK', 'AZ', 'AR', 'CA', 'CO', 'CT', 'DE', 'FL', 'GA', 
    'HI', 'ID', 'IL', 'IN', 'IA', 'KS', 'KY', 'LA', 'ME', 'MD', 
    'MA', 'MI', 'MN', 'MS', 'MO', 'MT', 'NE', 'NV', 'NH', 'NJ', 
    'NM', 'NY', 'NC', 'ND', 'OH', 'OK', 'OR', 'PA', 'RI', 'SC', 
    'SD', 'TN', 'TX', 'UT', 'VT', 'VA', 'WA', 'WV', 'WI', 'WY'
]

# Generate a list of 100 unique fake words
unique_words = set()
while len(unique_words) < 100:
    unique_words.add(fake.word())

# Convert the set to a list to enable indexing
unique_word_list = list(unique_words)

def get_random_word():
    # Randomly select a word from the pre-generated list
    return random.choice(unique_word_list)

def generate_uuid():
    return str(uuid.uuid4())

def generate_data():
    category = random.choice(categories)
    state_code = random.choice(states)
    sale_price = round(random.uniform(5, 1000), 2)  # Generates a float between $5 and $1000 with two decimal places

    return {
        'sale_id': generate_uuid(),
        'item_id': str(random.randint(1, 100)),
        'quantity': random.randint(1, 10),
        'sale_time': datetime.now(timezone.utc).strftime('%Y-%m-%d %H:%M:%S'),
        'store_id': str(random.randint(1, 1000)),
        'sale_price': sale_price,
        'product_name': get_random_word(),
        'product_category': category,
        'state_code': state_code,
    }

def send_data_to_kinesis(stream_name, data):
    try:
        response = kinesis.put_record(
            StreamName=stream_name,
            Data=json.dumps(data),
            PartitionKey="partitionkey"  # For demo purposes, a static partition key is used
        )
        print(f"Sent data to Kinesis: {data}, Shard ID: {response['ShardId']}")
    except Exception as e:
        print(f"Failed to send data to Kinesis: {e}")

def signal_handler(sig, frame):
    print('Gracefully shutting down...')
    sys.exit(0)

if __name__ == "__main__":
    signal.signal(signal.SIGINT, signal_handler)
    stream_name = 'amchauhan-demo-1'
    while True:
        data = generate_data()
        send_data_to_kinesis(stream_name, data)
        time.sleep(0.1)  # Sleep for 1 second to throttle the data sending rate
