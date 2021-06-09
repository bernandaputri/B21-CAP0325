
## Machine Learning

### Using Docker Image
STEPS:
1. Install docker on VM and open http
2. pull image from agungw9/bangkit
	```
    docker pull agungw9/bangkit
    ```
3. Run image
	```
    docker run -d --restart always -p 80:5000 --name bangkit agungw9/bangkit
    ```
    


### Build model from scratch
STEPS:
1. Open Google Colab
2. upload file Damage_Classificaition.ipynb
3. Run all using GPU
4. Export model to VM
5. Install virtual-env and necessary library from requirements.txt
	```
    apt-get install virtualenv
    virtualenv .
    source ./bin/activate
    pip3 install -r requirements.txt
    ```
6. lauch app.py
	```
    python3 -m flask run --host=0.0.0.0
    ```