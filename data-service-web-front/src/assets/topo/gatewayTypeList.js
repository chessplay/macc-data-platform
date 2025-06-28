define(function(require, exports, module){
  module.exports = {
      "EG680-P":{
        ports: [
            [{'port':'0', 'desc':'WAN0','type':'Gi'},{'port':'2', 'desc':'LAN2','type':'Gi'},{'port':'4', 'desc':'LAN4','type':'Gi'},{'port':'6', 'desc':'LAN6','type':'Gi'}],
            [{'port':'1', 'desc':'LAN1/WAN1','type':'Gi'},{'port':'3', 'desc':'LAN3','type':'Gi'},{'port':'5', 'desc':'LAN5','type':'Gi'},{'port':'7', 'desc':'LAN7','type':'Gi'}]
        ]
 	  },
       "EG2000P":{
            ports: [
                [{'port':'0', 'desc':'0/MGMT','type':'Gi'},{'port':'1',  'desc':'1','type':'Gi'},{'port':'2', 'desc':'2','type':'Gi'},{'port':'3', 'desc':'3','type':'Gi'},{'port':'4', 'desc':'4','type':'Gi'},{'port':'5', 'desc':'5','type':'Gi'},{'port':'6', 'desc':'6','type':'Gi'},{'port':'7', 'desc':'7','type':'Gi'},null,{'port':'6F', 'desc':'6F','type':'Gi'},{'port':'7F', 'desc':'7F','type':'Gi'}]
            ]
       },
        "EG2000SE":{
            ports: [
                [{'port':'0', 'desc':'0/MGMT','type':'Gi'},{'port':'1', 'desc':'1','type':'Gi'},{'port':'2', 'desc':'2','type':'Gi'},{'port':'3', 'desc':'3','type':'Gi'},{'port':'4', 'desc':'4','type':'Gi'},{'port':'5', 'desc':'5','type':'Gi'},{'port':'6', 'desc':'6','type':'Gi'},{'port':'7', 'desc':'7','type':'Gi'},null,{'port':'6F', 'desc':'6F','type':'Gi'},{'port':'7F', 'desc':'7F','type':'Gi'}]
            ]
        },
        "EG2000T":{
            ports: [
                [{'port':'0', 'desc':'0/MGMT','type':'Gi'},{'port':'1', 'desc':'1','type':'Gi'},{'port':'2', 'desc':'2','type':'Gi'},{'port':'3', 'desc':'3','type':'Gi'},{'port':'4', 'desc':'4','type':'Gi'},{'port':'5', 'desc':'5','type':'Gi'},{'port':'6', 'desc':'6','type':'Gi'},{'port':'7', 'desc':'7','type':'Gi'},null,{'port':'6F', 'desc':'6F','type':'Gi'},{'port':'7F', 'desc':'7F','type':'Gi'}]
            ]
        },
        "EG2000UE":{
            ports: [
                [{'port':'1', 'desc':'1','type':'Gi'},{'port':'3', 'desc':'3','type':'Gi'},{'port':'5', 'desc':'5','type':'Gi'},{'port':'7', 'desc':'7','type':'Gi'},null,{'port':'1F', 'desc':'1F','type':'Gi'},{'port':'3', 'desc':'3F','type':'Gi'},null,{'port':'5', 'desc':'5F','type':'Gi'},{'port':'7', 'desc':'7F','type':'Gi'},null,{'port':'1F', 'desc':'1F','type':'Te'},{'port':'3F', 'desc':'3F','type':'Te'}],
                [{'port':'0', 'desc':'0','type':'Gi'},{'port':'2', 'desc':'2','type':'Gi'},{'port':'4', 'desc':'4','type':'Gi'},{'port':'6', 'desc':'6','type':'Gi'},null,{'port':'0F', 'desc':'0F','type':'Gi'},{'port':'2', 'desc':'2F','type':'Gi'},null,{'port':'4', 'desc':'4F','type':'Gi'},{'port':'6', 'desc':'6F','type':'Te'},null,{'port':'0F', 'desc':'0F'},{'port':'2F', 'desc':'2F','type':'Te'}]
            ]
        },
        "EG2000X":{
            ports: [
                [{'port':'1', 'desc':'1','type':'Gi'},{'port':'3', 'desc':'3','type':'Gi'},{'port':'5', 'desc':'5','type':'Gi'},{'port':'7', 'desc':'7','type':'Gi'},null,{'port':'1F', 'desc':'1F','type':'Gi'},{'port':'3', 'desc':'3F','type':'Gi'},null,{'port':'5', 'desc':'5F','type':'Gi'},{'port':'7', 'desc':'7F','type':'Gi'},null,{'port':'1F', 'desc':'1F','type':'Te'},{'port':'3F', 'desc':'3F','type':'Te'}],
                [{'port':'0', 'desc':'0','type':'Gi'},{'port':'2', 'desc':'2','type':'Gi'},{'port':'4', 'desc':'4','type':'Gi'},{'port':'6', 'desc':'6','type':'Gi'},null,{'port':'0F', 'desc':'0F','type':'Gi'},{'port':'2', 'desc':'2F','type':'Gi'},null,{'port':'4', 'desc':'4F','type':'Gi'},{'port':'6', 'desc':'6F','type':'Te'},null,{'port':'0F', 'desc':'0F'},{'port':'2F', 'desc':'2F','type':'Te'}]
            ]
        },
        "EG2000XE":{
            ports: [
                [{'port':'1', 'desc':'1','type':'Gi'},{'port':'3', 'desc':'3','type':'Gi'},{'port':'5', 'desc':'5','type':'Gi'},{'port':'7', 'desc':'7','type':'Gi'},null,{'port':'1F', 'desc':'1F','type':'Gi'},{'port':'3F', 'desc':'3F','type':'Gi'},null,{'port':'5F', 'desc':'5F','type':'Gi'},{'port':'7F', 'desc':'7F','type':'Gi'},null,{'port':'1F', 'desc':'1F','type':'Te'},{'port':'3F', 'desc':'3F','type':'Te'}],
                [{'port':'0', 'desc':'0','type':'Gi'},{'port':'2', 'desc':'2','type':'Gi'},{'port':'4', 'desc':'4','type':'Gi'},{'port':'6', 'desc':'6','type':'Gi'},null,{'port':'0F', 'desc':'0F','type':'Gi'},{'port':'2F', 'desc':'2F','type':'Gi'},null,{'port':'4F', 'desc':'4F','type':'Gi'},{'port':'6F', 'desc':'6F','type':'Te'},null,{'port':'0F', 'desc':'0F'},{'port':'2F', 'desc':'2F','type':'Te'}]
            ]
        },
        "EG2100-P":{
            ports: [
                [{'port':'0', 'desc':'WAN0','type':'Gi'},{'port':'2', 'desc':'LAN2','type':'Gi'},{'port':'4', 'desc':'LAN4','type':'Gi'},{'port':'6', 'desc':'LAN6','type':'Gi'}],
                [{'port':'1', 'desc':'LAN1/WAN1','type':'Gi'},{'port':'3', 'desc':'LAN3','type':'Gi'},{'port':'5', 'desc':'LAN5','type':'Gi'},{'port':'7', 'desc':'LAN7','type':'Gi'}]
            ]
        },
        "EG3000CE":{
            ports: [
                [{'port':'0', 'desc':'0/MGMT','type':'Gi'},{'port':'1', 'desc':'1','type':'Gi'},{'port':'2', 'desc':'2','type':'Gi'},{'port':'3', 'desc':'3','type':'Gi'},{'port':'4', 'desc':'4','type':'Gi'},{'port':'5', 'desc':'5','type':'Gi'},{'port':'6', 'desc':'6','type':'Gi'},{'port':'7', 'desc':'7','type':'Gi'},null,{'port':'4F', 'desc':'4F','type':'Gi'},{'port':'5F', 'desc':'5F','type':'Gi'},{'port':'6F', 'desc':'6F','type':'Gi'},{'port':'7F', 'desc':'7F','type':'Gi'}]
            ]
        },
        "EG3000GE":{
            ports: [
                [{'port':'0', 'desc':'0/MGMT','type':'Gi'},{'port':'1', 'desc':'1','type':'Gi'},{'port':'2', 'desc':'2','type':'Gi'},{'port':'3', 'desc':'3','type':'Gi'},{'port':'4', 'desc':'4','type':'Gi'},{'port':'5', 'desc':'5','type':'Gi'},{'port':'6', 'desc':'6','type':'Gi'},{'port':'7', 'desc':'7','type':'Gi'},null,{'port':'4F', 'desc':'4F','type':'Gi'},{'port':'5F', 'desc':'5F','type':'Gi'},{'port':'6F', 'desc':'6F','type':'Gi'},{'port':'7F', 'desc':'7F','type':'Gi'}]
            ]
        },
        "EG3000ME":{
            ports: [
                [{'port':'0', 'desc':'0/MGMT','type':'Gi'},{'port':'1', 'desc':'1','type':'Gi'},{'port':'2', 'desc':'2','type':'Gi'},{'port':'3', 'desc':'3','type':'Gi'},{'port':'4', 'desc':'4','type':'Gi'},{'port':'5', 'desc':'5','type':'Gi'},{'port':'6', 'desc':'6','type':'Gi'},{'port':'7', 'desc':'7','type':'Gi'},null,{'port':'4F', 'desc':'4F','type':'Gi'},{'port':'5F', 'desc':'5F','type':'Gi'},{'port':'6F', 'desc':'6F','type':'Gi'},{'port':'7F', 'desc':'7F','type':'Gi'}]
            ]
        },
        "EG3000SE":{
            ports: [
                [{'port':'0', 'desc':'0/MGMT','type':'Gi'},{'port':'1', 'desc':'1','type':'Gi'},{'port':'2', 'desc':'2','type':'Gi'},{'port':'3', 'desc':'3','type':'Gi'},{'port':'4', 'desc':'4','type':'Gi'},{'port':'5', 'desc':'5','type':'Gi'},{'port':'6', 'desc':'6','type':'Gi'},{'port':'7', 'desc':'7','type':'Gi'},null,{'port':'4F', 'desc':'4F','type':'Gi'},{'port':'5F', 'desc':'5F','type':'Gi'},{'port':'6F', 'desc':'6F','type':'Gi'},{'port':'7F', 'desc':'7F','type':'Gi'}]
            ]
        },
        "EG3000UE":{
            ports: [
                [{'port':'1F', 'desc':'1','type':'Te'},{'port':'3F', 'desc':'3','type':'Te'},{'port':'5F', 'desc':'5','type':'Te'},{'port':'7', 'desc':'7','type':'Te'},{'port':'1', 'desc':'1','type':'Gi'},{'port':'3F', 'desc':'3','type':'Gi'},{'port':'5F', 'desc':'5','type':'Gi'},{'port':'7F', 'desc':'7','type':'Gi'}, null,null,{'port':'1', 'desc':'1','type':'Gi'},{'port':'3', 'desc':'3','type':'Gi'},{'port':'5', 'desc':'5','type':'Gi'},{'port':'7', 'desc':'7/MGMT','type':'Gi'}],
                [{'port':'0F', 'desc':'0','type':'Te'},{'port':'2F', 'desc':'2','type':'Te'},{'port':'4F', 'desc':'4','type':'Te'},{'port':'6F', 'desc':'6','type':'Te'},{'port':'0F', 'desc':'0','type':'Gi'},{'port':'2F', 'desc':'2','type':'Gi'},{'port':'4F', 'desc':'4','type':'Gi'},{'port':'6F', 'desc':'6','type':'Gi'}, null,null,{'port':'0', 'desc':'0','type':'Gi'},{'port':'2', 'desc':'2','type':'Gi'},{'port':'4', 'desc':'4','type':'Gi'},{'port':'6', 'desc':'6','type':'Gi'}]
            ]
        },
        "EG3000XE":{
            ports: [
                [{'port':'1F', 'desc':'1','type':'Te'},{'port':'3F', 'desc':'3','type':'Te'},{'port':'5F', 'desc':'5','type':'Te'},{'port':'7F', 'desc':'7','type':'Te'},{'port':'1F', 'desc':'1','type':'Gi'},{'port':'3F', 'desc':'3','type':'Gi'},{'port':'5F', 'desc':'5','type':'Gi'},{'port':'7F', 'desc':'7','type':'Gi'}, null,null,{'port':'1', 'desc':'1','type':'Gi'},{'port':'3', 'desc':'3','type':'Gi'},{'port':'5', 'desc':'5','type':'Gi'},{'port':'7', 'desc':'7/MGMT','type':'Gi'}],
                [{'port':'0F', 'desc':'0','type':'Te'},{'port':'2F', 'desc':'2','type':'Te'},{'port':'4F', 'desc':'4','type':'Te'},{'port':'6F', 'desc':'6','type':'Te'},{'port':'0F', 'desc':'0','type':'Gi'},{'port':'2F', 'desc':'2','type':'Gi'},{'port':'4F', 'desc':'4','type':'Gi'},{'port':'6F', 'desc':'6','type':'Gi'}, null,null,{'port':'0', 'desc':'0','type':'Gi'},{'port':'2', 'desc':'2','type':'Gi'},{'port':'4', 'desc':'4','type':'Gi'},{'port':'6', 'desc':'6','type':'Gi'}]
            ]
        },
        "EG3210":{
            ports: [
                [{'port':'0', 'desc':'0/MGMT','type':'Gi'},{'port':'1', 'desc':'1','type':'Gi'},{'port':'2', 'desc':'2','type':'Gi'},{'port':'3', 'desc':'3','type':'Gi'},{'port':'4', 'desc':'4','type':'Gi'},{'port':'5', 'desc':'5','type':'Gi'},{'port':'6', 'desc':'6','type':'Gi'},{'port':'7', 'desc':'7','type':'Gi'},{'port':'8F', 'desc':'8F','type':'Gi'},{'port':'9F', 'desc':'9F','type':'Gi'}]
            ]
        },
        "EG3220":{
                ports: [
                [{'port':'0', 'desc':'0/MGMT','type':'Gi'},{'port':'1', 'desc':'1','type':'Gi'},{'port':'2', 'desc':'2','type':'Gi'},{'port':'3', 'desc':'3','type':'Gi'},{'port':'4', 'desc':'4','type':'Gi'},{'port':'5', 'desc':'5','type':'Gi'},{'port':'6', 'desc':'6','type':'Gi'},{'port':'7', 'desc':'7','type':'Gi'},{'port':'0F', 'desc':'8F','type':'Te'},{'port':'9F', 'desc':'9F','type':'Gi'}]
            ]
        },
        "EG3230":{
                ports: [
                [{'port':'0', 'desc':'0/MGMT','type':'Gi'},{'port':'1', 'desc':'1','type':'Gi'},{'port':'2', 'desc':'2','type':'Gi'},{'port':'3', 'desc':'3','type':'Gi'},{'port':'4', 'desc':'4','type':'Gi'},{'port':'5', 'desc':'5','type':'Gi'},{'port':'6', 'desc':'6','type':'Gi'},{'port':'7', 'desc':'7','type':'Gi'},{'port':'0F', 'desc':'8F','type':'Te'},{'port':'9F', 'desc':'9F','type':'Gi'}]
            ]
        },
        "EG3250":{
            ports: [
                [{'port':'0', 'desc':'0/MGMT','type':'Gi'},{'port':'1', 'desc':'1','type':'Gi'},{'port':'2', 'desc':'2','type':'Gi'},{'port':'3', 'desc':'3','type':'Gi'},{'port':'4', 'desc':'4','type':'Gi'},{'port':'5', 'desc':'5','type':'Gi'},{'port':'6', 'desc':'6','type':'Gi'},{'port':'7', 'desc':'7','type':'Gi'},{'port':'0F', 'desc':'8F','type':'Te'},{'port':'9F', 'desc':'9F','type':'Gi'}]
            ]
        },
        "EG1000C":{
            ports: [
                [{'port':'0', 'desc':'0/MGMT','type':'Gi'},{'port':'1', 'desc':'1','type':'Gi'},{'port':'2', 'desc':'2','type':'Gi'},{'port':'3', 'desc':'3','type':'Gi'},{'port':'4', 'desc':'4','type':'Gi'},{'port':'5', 'desc':'5','type':'Gi'},{'port':'6', 'desc':'6','type':'Gi'},null,{'port':'5F', 'desc':'5F','type':'Gi'},null,{'port':'6F', 'desc':'6F','type':'Gi'}]
            ]
        },
        "EG2000CE":{
            ports: [
                [{'port':'0', 'desc':'0/MGMT','type':'Gi'},{'port':'1', 'desc':'1','type':'Gi'},{'port':'2', 'desc':'2','type':'Gi'},{'port':'3', 'desc':'3','type':'Gi'},{'port':'4', 'desc':'4','type':'Gi'},{'port':'5', 'desc':'5','type':'Gi'},{'port':'6', 'desc':'6','type':'Gi'},{'port':'7', 'desc':'7','type':'Gi'},{'port':'6F', 'desc':'6F','type':'Gi'},{'port':'7F', 'desc':'7F','type':'Gi'}]
            ]
        },
        "EG2000CE-CM":{
            ports: [
                [{'port':'0', 'desc':'LAN0','type':'Gi'},{'port':'1', 'desc':'LAN1/WAN6','type':'Gi'},{'port':'2', 'desc':'LAN2/WAN5','type':'Gi'},{'port':'3', 'desc':'LAN3/WAN4','type':'Gi'},{'port':'4', 'desc':'LAN4/WAN3','type':'Gi'},{'port':'5', 'desc':'LAN5/WAN2','type':'Gi'},{'port':'6', 'desc':'LAN6/WAN1','type':'Gi'},{'port':'7', 'desc':'WAN0','type':'Gi'},{'port':'6F', 'desc':'LAN6/WAN1','type':'Gi'},{'port':'7F', 'desc':'WAN/0','type':'Gi'}]
            ]
        },
        "EG2000D":{
            ports: [
                [{'port':'0', 'desc':'0/MGMT','type':'Gi'},{'port':'1', 'desc':'1','type':'Gi'},{'port':'2', 'desc':'2','type':'Gi'},{'port':'3', 'desc':'3','type':'Gi'},{'port':'4', 'desc':'4','type':'Gi'},{'port':'5', 'desc':'5','type':'Gi'},{'port':'6', 'desc':'6','type':'Gi'},{'port':'7', 'desc':'7','type':'Gi'},{'port':'6F', 'desc':'6F','type':'Gi'},{'port':'7F', 'desc':'7F','type':'Gi'}]
            ]
        },
        "EG2000F":{
            ports: [
                [{'port':'0', 'desc':'LAN 0','type':'Gi'},{'port':'1', 'desc':'LAN1/WAN3','type':'Gi'},{'port':'2', 'desc':'LAN2/WAN2','type':'Gi'},{'port':'3', 'desc':'LAN3/WAN1','type':'Gi'},{'port':'4', 'desc':'WAN0','type':'Gi'}]
            ]
        },
        "EG2000G":{
            ports: [
                [{'port':'0', 'desc':'0/MGMT','type':'Gi'},{'port':'1', 'desc':'1','type':'Gi'},{'port':'2', 'desc':'2','type':'Gi'},{'port':'3', 'desc':'3','type':'Gi'},{'port':'4', 'desc':'4','type':'Gi'},{'port':'5', 'desc':'5','type':'Gi'},{'port':'6', 'desc':'6','type':'Gi'},{'port':'7', 'desc':'7','type':'Gi'},null,{'port':'4F', 'desc':'4F','type':'Gi'},
                {'port':'5F', 'desc':'5F','type':'Gi'},{'port':'6F', 'desc':'6F','type':'Gi'},{'port':'7F', 'desc':'7F','type':'Gi'}]
            ]
        },
        "EG2000GE":{
            ports: [
                [{'port':'0', 'desc':'0/MGMT','type':'Gi'},{'port':'1', 'desc':'1','type':'Gi'},{'port':'2', 'desc':'2','type':'Gi'},{'port':'3', 'desc':'3','type':'Gi'},{'port':'4', 'desc':'4','type':'Gi'},{'port':'5', 'desc':'5','type':'Gi'},{'port':'6', 'desc':'6','type':'Gi'},{'port':'7', 'desc':'7','type':'Gi'},null,{'port':'4F', 'desc':'4F','type':'Gi'},
                {'port':'5F', 'desc':'5F','type':'Gi'},{'port':'6F', 'desc':'6F','type':'Gi'},{'port':'7F', 'desc':'7F','type':'Gi'}]
            ]
        },
        "EG2000K":{
            ports: [
                [{'port':'0', 'desc':'LAN0','type':'Gi'},{'port':'1', 'desc':'LAN1/WAN5','type':'Gi'},{'port':'2', 'desc':'LAN2/WAN4','type':'Gi'},{'port':'3', 'desc':'LAN3/WAN3','type':'Gi'},{'port':'4', 'desc':'LAN4/WAN2','type':'Gi'},{'port':'5', 'desc':'WAN1','type':'Gi'},{'port':'6', 'desc':'WAN0','type':'Gi'},null,{'port':'5F', 'desc':'WAN1F','type':'Gi'},{'port':'6F', 'desc':'WAN0F','type':'Gi'}]
            ]
        },
        "NBR3000G-S":{
            ports: [
                [{'port':'0', 'desc':'LAN0','type':'Gi'}, {'port':'1', 'desc':'LAN1/WAN6','type':'Gi'}, {'port':'2', 'desc':'LAN2/WAN5','type':'Gi'},
                {'port':'3', 'desc':'LAN3/WAN4','type':'Gi'}, {'port':'4', 'desc':'LAN4/WAN3','type':'Gi'}, {'port':'5', 'desc':'LAN5/WAN2','type':'Gi'},
                {'port':'6', 'desc':'LAN6/WAN1','type':'Gi'}, {'port':'7', 'desc':'WAN0','type':'Gi'}, null,
                {'port':'6F', 'desc':'LAN6/WAN1','type':'Gi'}, {'port':'7F', 'desc':'WAN0','type':'Gi'}
                ]
            ]
        },
        "NBR3000D-UE":{
            ports: [
                [{'port':'0', 'desc':'LAN0','type':'Gi'}, {'port':'1', 'desc':'LAN1/WAN6','type':'Gi'}, {'port':'2', 'desc':'LAN2/WAN5','type':'Gi'},
                {'port':'3', 'desc':'LAN3/WAN4','type':'Gi'}, {'port':'4', 'desc':'LAN4/WAN3','type':'Gi'}, {'port':'5', 'desc':'LAN5/WAN2','type':'Gi'},
                {'port':'6', 'desc':'LAN6/WAN1','type':'Gi'}, {'port':'7', 'desc':'WAN0','type':'Gi'}, null,
                {'port':'6F', 'desc':'LAN6/WAN1','type':'Gi'}, {'port':'7F', 'desc':'WAN0','type':'Gi'}
                ]
            ]
        },
        "NBR6205-E":{
            ports: [
                [{'port':'0', 'desc':'LAN0/MGMT','type':'Gi'}, {'port':'1', 'desc':'LAN1/WAN6','type':'Gi'}, {'port':'2', 'desc':'LAN2/WAN5','type':'Gi'},
                {'port':'3', 'desc':'LAN3/WAN4','type':'Gi'}, {'port':'4', 'desc':'LAN4/WAN3','type':'Gi'}, {'port':'5', 'desc':'LAN5/WAN2','type':'Gi'},
                {'port':'6', 'desc':'LAN6/WAN1','type':'Gi'}, {'port':'7', 'desc':'WAN0','type':'Gi'}, null,
                {'port':'8F', 'desc':'8F','type':'Gi'}, {'port':'9F', 'desc':'9F','type':'Gi'}
                ]
            ]
        },
        "NBR6210-E":{
            ports: [
                [{'port':'0', 'desc':'LAN0/MGMT','type':'Gi'}, {'port':'1', 'desc':'LAN1/WAN6','type':'Gi'}, {'port':'2', 'desc':'LAN2/WAN5','type':'Gi'},
                {'port':'3', 'desc':'LAN3/WAN4','type':'Gi'}, {'port':'4', 'desc':'LAN4/WAN3','type':'Gi'}, {'port':'5', 'desc':'LAN5/WAN2','type':'Gi'},
                {'port':'6', 'desc':'LAN6/WAN1','type':'Gi'}, {'port':'7', 'desc':'WAN0','type':'Gi'}, null,
                {'port':'8F', 'desc':'8F','type':'Gi'}, {'port':'9F', 'desc':'9F','type':'Gi'}
                ]
            ]
        },
        "NBR6215-E":{
            ports: [
                [{'port':'0', 'desc':'LAN0/MGMT','type':'Gi'}, {'port':'1', 'desc':'LAN1/WAN6','type':'Gi'}, {'port':'2', 'desc':'LAN2/WAN5','type':'Gi'},
                {'port':'3', 'desc':'LAN3/WAN4','type':'Gi'}, {'port':'4', 'desc':'LAN4/WAN3','type':'Gi'}, {'port':'5', 'desc':'LAN5/WAN2','type':'Gi'},
                {'port':'6', 'desc':'LAN6/WAN1','type':'Gi'}, {'port':'7', 'desc':'WAN0','type':'Gi'}, null,
                {'port':'0F', 'desc':'8F','type':'Te'}, {'port':'9F', 'desc':'9F','type':'Gi'}
                ]
            ]
        },
        "NBR800G":{
            ports: [
                [{'port':'0', 'desc':'LAN0','type':'Gi'}, {'port':'1', 'desc':'LAN1/WAN3','type':'Gi'}, {'port':'2', 'desc':'LAN2/WAN2','type':'Gi'},
                {'port':'3', 'desc':'LAN3/WAN1','type':'Gi'}, {'port':'4', 'desc':'WAN0','type':'Gi'}
                ]
            ]
        }, 
        "NBR950G":{
            ports: [
                [{'port':'0', 'desc':'LAN0','type':'Gi'}, {'port':'1', 'desc':'LAN1/WAN3','type':'Gi'}, {'port':'2', 'desc':'LAN2/WAN2','type':'Gi'},
                {'port':'3', 'desc':'LAN3/WAN1','type':'Gi'}, {'port':'4', 'desc':'WAN0','type':'Gi'}
                ]
            ]
        },
       "EG350":{
         	ports  : [
             	[{'port':'0', 'desc':'0/MGMT','type':'Gi'},{'port':'1', 'desc':'1','type':'Gi'},{'port':'2', 'desc':'2','type':'Gi'},{'port':'3', 'desc':'3','type':'Gi'},{'port':'4', 'desc':'4','type':'Gi'},{'port':'5', 'desc':'5','type':'Gi'}]
         	]
     	},
       "EG350-V":{
         	ports  : [
             	[{'port':'0', 'desc':'0/MGMT','type':'Gi'},{'port':'1', 'desc':'1','type':'Gi'},{'port':'2', 'desc':'2','type':'Gi'},{'port':'3', 'desc':'3','type':'Gi'},{'port':'4', 'desc':'4','type':'Gi'},{'port':'5', 'desc':'5','type':'Gi'}]
         	]
     	},
        "EG580-W":{
            ports   : [
                [{'port':'1', 'desc':'LAN1','type':'Gi'},null,{'port':'2', 'desc':'WAN4/LAN2','type':'Gi'},{'port':'3', 'desc':'WAN3/LAN3','type':'Gi'},{'port':'4', 'desc':'WAN2/LAN4','type':'Gi'},{'port':'5', 'desc':'WAN1','type':'Gi'}]
            ]
        },
        "NBR880GW":{
            ports   : [
                [{'port':'1', 'desc':'LAN1','type':'Gi'},null,{'port':'2', 'desc':'WAN4/LAN2','type':'Gi'},{'port':'3', 'desc':'WAN3/LAN3','type':'Gi'},{'port':'4', 'desc':'WAN2/LAN4','type':'Gi'},{'port':'5', 'desc':'WAN1','type':'Gi'}]
            ]
        },
       "NBR1000G":{
            ports   : [
                [{'port':'0', 'desc':'LAN0','type':'Gi'},{'port':'1', 'desc':'LAN1/WAN5','type':'Gi'},{'port':'2', 'desc':'LAN2/WAN4','type':'Gi'},{'port':'3', 'desc':'WAN1','type':'Gi'},{'port':'4', 'desc':'WAN0','type':'Gi'}]
            ]
       },
       "NBR1000G-C":{
                ports   : [
                    [{'port':'0', 'desc':'LAN0','type':'Gi'},{'port':'1', 'desc':'LAN1/WAN3','type':'Gi'},{'port':'2', 'desc':'LAN2/WAN2','type':'Gi'},{'port':'3', 'desc':'LAN3/WAN1','type':'Gi'},{'port':'4', 'desc':'WAN0','type':'Gi'}]
                ]
           },
       "NBR1000G-E":{
            ports   : [
                [{'port':'0', 'desc':'LAN0','type':'Gi'},{'port':'1', 'desc':'LAN1/WAN3','type':'Gi'},{'port':'2', 'desc':'LAN2/WAN2','type':'Gi'},{'port':'3', 'desc':'LAN3/WAN1','type':'Gi'},{'port':'4', 'desc':'WAN0','type':'Gi'}]
            ]
       },
       "NBR108G-P":{
            ports   : [
                [{'port':'0', 'desc':'WAN0','type':'Gi'},{'port':'2', 'desc':'LAN2','type':'Gi'},{'port':'4', 'desc':'LAN4','type':'Gi'},{'port':'6', 'desc':'LAN6','type':'Gi'}],
                [{'port':'1', 'desc':'LAN1/WAN1','type':'Gi'},{'port':'3', 'desc':'LAN3','type':'Gi'},{'port':'5', 'desc':'LAN5','type':'Gi'},{'port':'7', 'desc':'LAN7','type':'Gi'}]
            ]
       },
       "NBR1300G-E":{
            ports   : [
                [{'port':'0', 'desc':'LAN0','type':'Gi'},{'port':'1', 'desc':'LAN1/WAN3','type':'Gi'},{'port':'2', 'desc':'LAN2/WAN2','type':'Gi'},{'port':'3', 'desc':'LAN3/WAN1','type':'Gi'},{'port':'4', 'desc':'WAN0','type':'Gi'}]
            ]
       },
        "NBR1700G-E":{
            ports   : [
                [{'port':'0', 'desc':'LAN0','type':'Gi'},{'port':'1', 'desc':'LAN1/WAN4','type':'Gi'},{'port':'2', 'desc':'LAN2/WAN3','type':'Gi'},{'port':'3', 'desc':'LAN3/WAN2','type':'Gi'},{'port':'4', 'desc':'LAN4/WAN1','type':'Gi'},{'port':'5', 'desc':'WAN0','type':'Gi'}]
            ]
       },
       "NBR2000G":{
            ports   : [
                [{'port':'0', 'desc':'LAN0','type':'Gi'},{'port':'1', 'desc':'LAN1/WAN5','type':'Gi'},{'port':'2', 'desc':'LAN2/WAN4','type':'Gi'},{'port':'3', 'desc':'LAN3/WAN3','type':'Gi'},{'port':'4', 'desc':'LAN4/WAN2','type':'Gi'},{'port':'5', 'desc':'WAN1','type':'Gi'},{'port':'6', 'desc':'WAN0','type':'Gi'}]
            ]
       },
       "NBR2000G-C":{
            ports   : [
                [{'port':'0', 'desc':'LAN0','type':'Gi'},{'port':'1', 'desc':'LAN1/WAN6','type':'Gi'},{'port':'2', 'desc':'LAN2/WAN5','type':'Gi'},{'port':'3', 'desc':'LAN3/WAN4','type':'Gi'},{'port':'4', 'desc':'LAN4/WAN3','type':'Gi'},{'port':'5', 'desc':'LAN5/WAN2','type':'Gi'},{'port':'6', 'desc':'LAN6/WAN1','type':'Gi'},{'port':'7', 'desc':'WAN0','type':'Gi'}]
            ]
       },
       "NBR2100G-E":{
            ports   : [
                [{'port':'0', 'desc':'LAN0','type':'Gi'},{'port':'1', 'desc':'LAN1/WAN6','type':'Gi'},{'port':'2', 'desc':'LAN2/WAN5','type':'Gi'},{'port':'3', 'desc':'LAN3/WAN4','type':'Gi'},{'port':'4', 'desc':'LAN4/WAN3','type':'Gi'},{'port':'5', 'desc':'LAN5/WAN2','type':'Gi'},{'port':'6', 'desc':'LAN6/WAN1','type':'Gi'},{'port':'7', 'desc':'WAN0','type':'Gi'}]
            ]
       },
       "NBR2500D-E":{
            ports   : [
                [{'port':'0', 'desc':'LAN0','type':'Gi'},{'port':'1', 'desc':'LAN1/WAN6','type':'Gi'},{'port':'2', 'desc':'LAN2/WAN5','type':'Gi'},{'port':'3', 'desc':'LAN3/WAN4','type':'Gi'},{'port':'4', 'desc':'LAN4/WAN3','type':'Gi'},{'port':'5', 'desc':'LAN5/WAN2','type':'Gi'},{'port':'6', 'desc':'LAN6/WAN1','type':'Gi'},{'port':'7', 'desc':'WAN0','type':'Gi'}]
            ]
       },
       "NBR3000D-E":{
            ports   : [
                [{'port':'0', 'desc':'LAN0','type':'Gi'},{'port':'1', 'desc':'LAN1/WAN6','type':'Gi'},{'port':'2', 'desc':'LAN2/WAN5','type':'Gi'},{'port':'3', 'desc':'LAN3/WAN4','type':'Gi'},{'port':'4', 'desc':'LAN4/WAN3','type':'Gi'},{'port':'5', 'desc':'LAN5/WAN2','type':'Gi'},{'port':'6', 'desc':'LAN6/WAN1','type':'Gi'},{'port':'7', 'desc':'WAN0','type':'Gi'}]
            ]
       },
       "EG550":{
            ports   : [
                [{'port':'0', 'desc':'0/MGMT','type':'Gi'},{'port':'1', 'desc':'1','type':'Gi'},{'port':'2', 'desc':'2','type':'Gi'},{'port':'3', 'desc':'3','type':'Gi'},{'port':'4', 'desc':'4','type':'Gi'},{'port':'5', 'desc':'5','type':'Gi'},null,{'port':'6F', 'desc':'6F','type':'Gi'},{'port':'7F', 'desc':'7F','type':'Gi'}]
            ]
       },
       "NBR6120-E":{
            ports:[
                [{'port':'0', 'desc':'LAN0','type':'Gi'},{'port':'1', 'desc':'LAN1/WAN3','type':'Gi'},{'port':'2', 'desc':'LAN2/WAN2','type':'Gi'},{'port':'3', 'desc':'LAN3/WAN1','type':'Gi'},{'port':'4', 'desc':'WAN0','type':'Gi'}]
            ]
       },
       "NBR6135-E":{
            ports:[
                [{'port':'0', 'desc':'LAN0/MGMT','type':'Gi'},{'port':'1', 'desc':'LAN1/WAN4','type':'Gi'},{'port':'2', 'desc':'LAN2/WAN3','type':'Gi'},{'port':'3', 'desc':'LAN3/WAN2','type':'Gi'},{'port':'4', 'desc':'LAN4/WAN1','type':'Gi'},{'port':'5', 'desc':'WAN0','type':'Gi'},{'port':'6F', 'desc':'6F','type':'Gi'}]
            ]
       },
       "EG105G":{
            portsAsync: true,
            ports:[
                [{'port':'1', 'desc':'LAN0','type':'Gi'},{'port':'2', 'desc':'LAN1','type':'Gi'},{'port':'3', 'desc':'LAN2','type':'Gi'},{'port':'4', 'desc':'LAN3/WAN1','type':'Gi'},null,{'port':'5', 'desc':'WAN0','type':'Gi'}]
            ]
       },
       "EG105G-P":{
            portsAsync: true,
            ports:[
                [{'port':'1', 'desc':'LAN0','type':'Gi'},{'port':'2', 'desc':'LAN1','type':'Gi'},{'port':'3', 'desc':'LAN2','type':'Gi'},{'port':'4', 'desc':'LAN3/WAN1','type':'Gi'},null,{'port':'5', 'desc':'WAN0','type':'Gi'}]
            ]
       },
       "EG210G-P":{
            portsAsync: true,
            ports:[
                [{'port':'1', 'desc':'LAN0','type':'Gi'},{'port':'2', 'desc':'LAN1','type':'Gi'},{'port':'3', 'desc':'LAN2','type':'Gi'},{'port':'4', 'desc':'LAN3','type':'Gi'},{'port':'5', 'desc':'LAN4','type':'Gi'},{'port':'6', 'desc':'LAN5','type':'Gi'},{'port':'7', 'desc':'LAN6','type':'Gi'},{'port':'8', 'desc':'LAN7','type':'Gi'},null,{'port':'9', 'desc':'LAN8/WAN1','type':'Gi'},{'port':'10', 'desc':'WAN0','type':'Gi'}]
            ]
       },
       "EG205G":{
            portsAsync: true,
            ports:[
                [{'port':'1', 'desc':'1','type':'Gi'},{'port':'2', 'desc':'2','type':'Gi'},{'port':'3', 'desc':'3','type':'Gi'},{'port':'4', 'desc':'4','type':'Gi'},{'port':'5', 'desc':'0','type':'Gi'}]
            ]
       },
       "EG105GW":{
            portsAsync: true,
            ports:[
                [{'port':'1', 'desc':'LAN0','type':'Gi'},{'port':'2', 'desc':'LAN1','type':'Gi'},{'port':'3', 'desc':'LAN2','type':'Gi'},{'port':'4', 'desc':'LAN3/WAN1','type':'Gi'},{'port':'5', 'desc':'WAN0','type':'Gi'}]
            ]
       }
  };
});