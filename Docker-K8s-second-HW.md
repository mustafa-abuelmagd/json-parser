HOME WORK

- base docker images for c/c++ : why a type of base images over another? why use "alphine" for example
	- python
	- kotlin
	- Perl
- limit image resource during the process of creation
- how to reduce docker image sizes?
- how to do multi-stage images?
- what is a vulnerability scan in docker? why do we need it? how to perform it?
	- Docker vulnerability scan is scanning docker images for vulnerabilities in the packages used in the image used to report any issues that are known before pushing the image to the image registry and before anyone uses them.
	- Vulnerability scan is done automatically when pushing images to `Dockerhub` if the option is enabled
	- It can also be done locally using builtin Docker security support 
	- one of the tools is `Snyk CLI` with `Snyk Container extension` within Docker Desktop, the command used is `dockerfile snyk container test <image-name>`. Snyk also provide tip for developers on how to fix the listed issues
	- It's not enough to depend on Container Orchestration services like K8s, although they enforce security constraints on to run the Pods. they do not offer much when it comes to the containers inside of Pods 



- what are docker volumes? why do we need it? how to implement it practically?
	- Docker volumes is a way to map a filesystem resource on the host machine to the container filesystem resources. I'ts used when there's a need to have the data generated in the container to be persistent if, for whatever the reason, the container stops running. It's also used to have the changes happening on the host machine's folder to be reflected instantly in the container
	- One use case I've personally done was to map the current project's folder to the container so whenever the code changes I don't need to rebuild the image again
	- Another use case would be to have some database entries, for testing for example. or have the data created by  the database container persistent, although I'm convinced that it's not a best practice to run database on containers  





- what are container lifecycle hooks? understand it well and prepare a presentation about it
	- A lifecycle hook, like in client application components, is a functionality that automatically runs at various life stages of a container, a lifecycle is like starting the container, connecting to it from the outside, stopping, or whenever an error occurs in the running process in the container
	- The Docker container states are: Created, Running, Paused, Restarting, Exited.
	- They are used By Orchestration tools like K8s to respond to lifecycle events, whether it be exited to spawn new Pods, etc
	- Two types of hooks are `PostStart`: executed immediately after the container created,  and `PreStop`: is executed immediately before a container is terminated whether it be due to a API request, management event (liveness/ startup), probe failure, preemption, resource contention or many other triggers 
	- Can be also used to log container events, implement clean-up scripts, or run asynchoronous tasks after a Pod joins the cluster
	- Lifecycle handlers are two types; `Exec`: a command that can be run inside of the container's filesystem.  And `HTTP`: An HTTP handler that executes an HTTP request to and endpoint on the container  , 


- why does the K8s talk to the pods instead of containers?
	- As Pods can hold multiple containers that are, in best practices, very related to eachother, it makes sense that if one container is terminated, the other related ones are terminated as well and all are spawn together again.
	- This raises the need to abstract away the communication with the Containers inside of a Pod



- what are the factors  on which the scoring of the new pods assignments depends (kubernates in action book)?
	- The factors would things like:
		- the amount of load on the worker node, 
		- the location of the source requests coming to the container; may be it's better to serve a new pod to a location where the network traffic to the service is heavier.
		- Resource requirement and the availability of those resources 
		- Also Pods can be assigned a priority levels, that can be big factor in the schedueling algorithms
		- Topology constraints; to distribute nodes across different nodes to ensure high availability 
		- Inter-Pod communication: if two or more Pods communicate with eachother more often, it can be wiser to assign them to the same worker node to reduce network latency



- what happens when you create a new replicaset with a name that is already present in another replicaset, do they get merged? or what happens
	- when you create any resource with the same name as an existing one that depends on the command used, it the command used is `create -f` it'll raise an error that the resource is already defined. if the command used is `apply -f` it'll override the existing resource with a new one with the same name




* to push to docker hub: 
	* create a dockerhub account 
	* create a repository
	* build the local dockerfile with the `username/repositoryname:tag`: `docker build -t mustafaabuelmagd/testing:first` to build the local docker file 
	* after the image is built and verified using the command `docker images` and the image is found, we can you ```docer push mustafaabulemagd/testing:first``` to push the image to dockerhub

* to create a pod and access it through kubernates
	* create a `deplyment.yaml` file with 
	
```
apiVersion: apps/v1
kind: Deployment
metadata:
  name: my-deployment
spec:
  replicas: 3
  selector:
    matchLabels:
      app: testing-nginx
  template:
    metadata:
      labels:
        app: testing-nginx
    spec:
      containers:
      - name: testing-nginx
        image: mustafaabuelmagd/testing:second

```
 where `mustafaabuelmagd/testing:first` is the image pushed to `dockerhub` from the previous step

* to list available `kubernates` deployments we use the command `kubectl get deployments`:![Screenshot from 2023-11-28 09-28-40.png](..%2F..%2F..%2F..%2FPictures%2FScreenshots%2FScreenshot%20from%202023-11-28%2009-28-40.png)
* to list available `kubernates` pods we use the command `kubectl get pods` :![Screenshot from 2023-11-28 09-29-26.png](..%2F..%2F..%2F..%2FPictures%2FScreenshots%2FScreenshot%20from%202023-11-28%2009-29-26.png)

* to get the available containers inside of a pod in kubernates we use the command `kubectl get pod <pod-name> -o json`: this outputs the pod information in a json format: 
  ```
  {
    "apiVersion": "v1",
    "kind": "Pod",
    "spec": {
        "containers": [
            {
                "image": "mustafaabuelmagd/testing:second",
                "imagePullPolicy": "IfNotPresent",
  ##############"name": "testing-nginx",#####################################
                "resources": {},
                "terminationMessagePath": "/dev/termination-log",
                "terminationMessagePolicy": "File",
                "volumeMounts": [
                    {
                        "mountPath": "/var/run/secrets/kubernetes.io/serviceaccount",
                        "name": "kube-api-access-8dbvn",
                        "readOnly": true
                    }
                ]
            }
        ],
    },
}

and from the pod specs we can get the container name `testing-nginx`
* to access the running container from the *browser environment* with that name we'll need to forward the ports first using the command `kubectl port-forward pod/<pod-name> 8080:80`  and we get the following ![Screenshot from 2023-11-29 09-09-33.png](..%2F..%2F..%2F..%2FPictures%2FScreenshots%2FScreenshot%20from%202023-11-29%2009-09-33.png)
* the difference between `create -f` and `apply -f` in `kubernates` is that 
	* `apply -f` is used to apply the changes in a `.yaml` file to the resource if the resource is bound. if the resource is not found; it creates it 
		* use case: `kubectl apply -f deployment.yaml`
	* `create -f` is used to create resources and returns error if the resource is present. if the existing resource needs to be updated; this cannot be done with `create -f`; the resource needs to be deleted and recreated
		* use case: when we want to create a new resource and we need to make sure that the resource is only created once and avoid any accedintal updates

* to delete a kubernates pod we use the command `kubectl delete pod <pod-name>`
	* getting the current existing pods `kubectl get pods`![Screenshot from 2023-11-29 10-00-04.png](..%2F..%2F..%2F..%2FPictures%2FScreenshots%2FScreenshot%20from%202023-11-29%2010-00-04.png)
	* deleting the pod with the name `my-deployment-74786fb4b7-2xnlh` using the command `kubectl delete my-deployment-74786fb4b7-2xnlh`, and then list available pods using `kubectl get pods` 
	* the pod `my-deployment-74786fb4b7-2xnlh` is no longer available, and another one with the name `my-deployment-74786fb4b7-6tlb8` is created

* creating a pod directly with the specs ![Screenshot from 2023-11-29 10-04-21.png](..%2F..%2F..%2F..%2FPictures%2FScreenshots%2FScreenshot%20from%202023-11-29%2010-04-21.png)
	* to create this resource we use the command `kubectl create -f pod.yaml` in the same folder
	* `kubectl get pods` now shows the new pod with the name  `my-pod`  ![Screenshot from 2023-11-29 10-05-53.png](..%2F..%2F..%2F..%2FPictures%2FScreenshots%2FScreenshot%20from%202023-11-29%2010-05-53.png)
	* trying to create the resource again using `create -f` will give an error ![Screenshot from 2023-11-29 10-07-56.png](..%2F..%2F..%2F..%2FPictures%2FScreenshots%2FScreenshot%20from%202023-11-29%2010-07-56.png)
	* to delete the created pod we use the command `kubectl delete pod my-pod`
	* now list available pods again using `kubectl get pods` shows that the created pod directly is not recreated or managed by kubernates ![Screenshot from 2023-11-29 10-09-58.png](..%2F..%2F..%2F..%2FPictures%2FScreenshots%2FScreenshot%20from%202023-11-29%2010-09-58.png)
