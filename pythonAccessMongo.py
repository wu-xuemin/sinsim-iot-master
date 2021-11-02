import pymongo
import time
import sys
import os

### 参数为铭牌号， JAVA调用该脚本去查询mongodb，按铭牌号查询iot_machine，6千万数据秒回。
#print(time.strftime("%Y-%m-%d %H:%M:%S",time.localtime()))

#os.makedirs("/home/sinsim_iot/mmm") 
#os.mknod("get-it.txt") 
#print("starting find")
client = pymongo.MongoClient(host='172.18.0.5', port=27017)

db = client.sinsim_iot
collection = db.iot_machine
#result = collection.find_one({'nameplate': 'Aliyun-OK'})
#results = collection.find({'nameplate': 'Aliyun-OK'})
results = collection.find({'nameplate': sys.argv[1]})
#results = "fangbian"# collection.find({'nameplate': sys.argv[1]})
#results = collection.find({'nameplate': 'loc11'})

#print(results)
for result in results:
        print(result)

        #print(time.strftime("%Y-%m-%d %H:%M:%S",time.localtime()))
        #print("done find")
