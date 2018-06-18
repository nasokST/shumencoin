import requests
import datetime
import hashlib
import binascii
import json

class Block(object):
    index = 0
    dificulty = 0
    nonce = 0
    creationDate = ""
    blockDataHash = ""
    blockHash = ""

    def __init__(self, index, dificulty, nonce, creationDate, blockDataHash, blockHash):
        self.index = index
        self.dificulty = dificulty
        self.nonce = nonce
        self.creationDate = creationDate
        self.blockDataHash = blockDataHash
        self.blockHash = blockHash


class Miner(object):
    def __init__(self, port, minerAddress):
        self.port = port
        self.minerAddress = minerAddress
        self.nodeHostUrl = f'http://127.0.0.1:{port}'
        self.askJobUrl = f'{self.nodeHostUrl}/mining/job-request/{self.minerAddress}'
        self.submitJobUrl = f'{self.nodeHostUrl}/mining/submit-new-block'

    def getMiningJob(self):

        try:
            response = requests.get(self.askJobUrl)

            if response.status_code == 200:
                targetBlock = Block(response.json()['index'], response.json()['dificulty'], response.json()['nonce'],
                                    response.json()['creationDate'], response.json()['blockDataHash'], response.json()['blockHash'])

                print("Get the new job SUCCESS")
                return targetBlock

            print("Can not get the new job")

        except requests.exceptions.ConnectionError:
            print("Connection FILURE")

        return None

    def submitJob(self, minedBlock):

        try:
            data = vars(minedBlock)
            params = {}

            response = requests.post(self.submitJobUrl, params, data)

            if response.status_code == 200:
                return True

        except requests.exceptions.ConnectionError:
            print("Connection FILURE")

        return False

    def isDifficultyValid(self, hash: str, difficulty: int):
        hash_difficulty_count: int = 0;

        if hash == None:
            return False

        i = 0
        while i < len(hash):
            if hash[i] != '0':
                break

            hash_difficulty_count += 1
            i+=1

        if hash_difficulty_count < difficulty:
            return False

        return True

    def mine(self, targetBlock):
        targetBlock.creationDate = datetime.datetime.utcnow().replace(tzinfo=datetime.timezone.utc).isoformat()
        targetBlock.nonce = 0

        while not self.isDifficultyValid(targetBlock.blockHash, targetBlock.dificulty):
            targetBlock.nonce+=1

            toHash = targetBlock.blockDataHash + targetBlock.creationDate + str(targetBlock.nonce)
            hash = hashlib.sha256(toHash.encode("utf8")).digest()
            targetBlock.blockHash = binascii.hexlify(hash).decode('utf-8')
            #print("BLOCKHASH: : ", targetBlock.blockHash)

        print("BLOCK Mined: ", targetBlock.blockHash)

        return targetBlock

    def startMmining(self):

        while True:
            targetBlock = self.getMiningJob()

            if None != targetBlock:
                minedBlock = self.mine(targetBlock)
                self.submitJob(minedBlock)




from argparse import ArgumentParser

parser = ArgumentParser()
parser.add_argument('-p', '--port', default=8080, type=int, help='port to listen on')
parser.add_argument('-a', '--address', default='c3293572dbe6ebc60de4a20ed0e21446cae66b17', type=str, help='miner address')
args = parser.parse_args()
port = args.port
address = args.address

miner = Miner(port, address)
miner.startMmining()

