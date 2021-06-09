## Cloud Computing

<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary><h2 style="display: inline-block">Table of Contents</h2></summary>
  <ol>
    <li><a href="#compute-engine">Compute Engine</a></li>
    <li><a href="#cloud-storage">Cloud Storage</a></li>
    <li><a href="#cloud-firestore">Cloud Firestore</a></li>

  </ol>
</details>


### Compute Engine
1. In the Google Cloud Console, go to the Create an instance page.

2. Go to Create an instance

3. Under Zone, select the zone where you want to host this instance. The Series list is filtered to include only the machine type families available in the selected zone.

4. Under Machine configuration, select General-purpose.

5. From the Series list, select N1 under First Generation for N1 custom machine types or E2, N2, or N2D for Second Generation custom machine types.

6. From the Machine type list, select Custom.

7. To specify the number of vCPUs and the amount of memory for the VM instance, drag the sliders or enter the values in the text boxes. The console displays an estimated cost for the instance as you change the number of vCPUs and memory.

8. Save your changes and continue to create the VM

### Cloud Storage
1. In the Google Cloud Console, go to the Cloud Storage Browser page
2. Click Create bucket.
3. On the Create a bucket page, enter your bucket information. To go to the next step, click Continue. <br/>
  For Name your bucket, enter a name that meets the bucket naming requirements.
  For Choose where to store your data, select a Location type and Location option where the bucket data will be permanently stored.<br/>
  For Choose a default storage class for your data, select a storage class for the bucket. The default storage class is assigned by default to all objects uploaded to the bucket.<br/>
  For Choose how to control access to objects, select an Access control option. The access control model determines how you control access to the bucket's objects.<br/>
  For Advanced settings (optional), add bucket labels, set a retention policy, and choose an encryption method.
4. Click Create

Using CLI : 
```
gsutil mb gs://projectlist1
creating gs://projectlist1/
gsutil mb -p PROJECT_ID -c STORAGE_CLASS -l BUCKET_LOCATION -b on gs://BUCKET_NAME
gsutil ls
gsutil ls -r gs://projectlist1/
```
Upload object
```
gsutil cp tet.jpg gs://projectlist1/
```
### Cloud Firestore
#### Add data
1. Click Start Collection.
2. Enter a collection ID. Enter a document ID. Firestore will generate document ID, but you can overwrite for a specific document ID. Add fields for the data in your document.
3. Click Save. Your new collection and document appear in the data viewer.
4. To add more documents to the collection, click Add Document.

#### Edit data
1. Click on a collection to view its documents, then click on a document to view its fields and subcollections.
2. Click on a field to edit its value. To add fields or subcollections to the selected document, click Add Field or Start Collection.

#### Delete data
To delete a collection:

1. Select the collection you want to delete.
2. Click the menu icon at the top of the documents column, then click Delete collection.

To delete a document:

1. Select the document you want to delete.
2. Click the menu icon at the top of the document details column. Select Delete document or Delete document fields

To delete a specific field in a document:

1. Select the document to view its fields.
2. Click the delete icon beside the field you want to delete.



