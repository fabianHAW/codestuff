from kubernetes import client, config

config.load_kube_config()

v1=client.CoreV1Api()
print("Listing rest-api pod with the IP and port:")
ret = v1.list_pod_for_all_namespaces(watch=False)
for i in ret.items:
    if 'rest-api' in i.metadata.name:
       print("%s\t%s\t%s\t%s" % (i.status.pod_ip, i.metadata.namespace, i.metadata.name, i.spec.containers[0].ports[0].container_port))
