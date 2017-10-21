'''
Created on Oct 18, 2017

@author: leo <dev@connectz.cn> 
'''

import json
from urllib import unquote   
from urllib import quote
from __builtin__ import int

class MyJson:
    
    def __init__(self, obj):
        self.obj = obj
       
        if isinstance(self.obj, str):
            try:
                self.obj = json.loads(self.obj)
            except Exception, e:
                print(e)
            
        #print(type(self.obj))    
        
    def decode(self):      
        obj = self.obj 
        if isinstance(obj, dict):
            for key in obj:
                value = obj[key];
                if isinstance(value, unicode):
                    value = value.encode('unicode-escape').decode('string_escape') 
                if isinstance(value, dict) or isinstance(value, list):
                    obj[key] = MyJson(value).decode()
                elif isinstance(value, str):
                    obj[key] = unquote(value)
        elif isinstance(obj, list):
            for i in range(len(obj)):
                obj[i] = MyJson(obj[i]).decode()   
                
        return obj
     
    def toString(self):  
        obj = self.obj 
        msg = ''
        if isinstance(obj, dict):
            msg = msg + '{'
            for key in obj:
                value = obj[key];
                if isinstance(value, unicode):
                    value = value.encode('unicode-escape').decode('string_escape')
                if isinstance(value, dict) or isinstance(value, list):
                    value = MyJson(value).toString()
                elif isinstance(value, str):
                    value = '"' + quote(value) + '"';
                elif isinstance(value, int):
                    value = str(value);
                else:
                    value = '"' + str(value) + '"';
                key = '"' + key + '"';
                msg = msg + key + ':' + value + ',';
            if msg.endswith((",")):
                msg = msg[0: (len(msg) - 1)];
            msg = msg + '}';
            
        elif isinstance(obj, list):
            msg = msg + '[';
            for i in range(len(obj)):
                value = obj[i];
                if isinstance(value, unicode):
                    value = value.encode('unicode-escape').decode('string_escape')
                value = MyJson(value).toString()
                msg = msg + value + ',';
                 
            if msg.endswith((",")):
                msg = msg[0, len(msg) - 1];
            msg = msg + ']';   
            
        return msg
        
        
        
        
        