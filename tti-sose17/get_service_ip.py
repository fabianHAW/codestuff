from kubernetes import client, config

config.load_kube_config()

v1=client.CoreV1Api()
print("Listing rest-api IP and port:")
ret = v1.list_namespaced_endpoints('default', watch=False)
for i in ret.items:
    if 'rest-api' == i.metadata.name:
       print('print ip and port via list index')
       print(i.subsets[0].addresses[0].ip)
       print(i.subsets[0].ports[0].port)
       print('print ip and port via list iteration')
       for sub in i.subsets:
          for addr in sub.addresses:
             print(addr.ip)
          for port in sub.ports:
             print(port.port) 
