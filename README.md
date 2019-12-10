# CS509-CardMaker

Link for the Project: [Card Maker by Team Justice](https://master.d6ecpvwgxvbq1.amplifyapp.com/).

## Project : Card Maker
Members: Hung Hong, Jianbing Yang, Sami Baral, Zhiyuan Chen

## Github
Backend (Java, AWS Lambda function) : https://github.com/imaslarab/CS509-CardMaker
Frontend (React hosted in AWS Amplity) : https://github.com/imaslarab/CS509-CardMaker-React

### Capabilities

#### 1. Card Construction Operations
  * Create Card for a given event and recipient:
    * View the application
    * Under "Create Card", fill up the necessary information (recipient, event type, and orientation)
    * Click "Create"
    * New card will be created and shown on the main dashboard
  * Delete Card for a given event and recipient:
	  * View the application
	  * Choose which card you want to delete from the main dashboard
	  * Click "Delete" under the card description.
	  * Card will be deleted from the main dashboard
  * Duplicate Card for a given event and recipient and assign to new recipient
	  * View the application
	  * Click "Duplicate Card"
	  * On the popup, select card to duplicate
	  * Fill up the new recipient for the duplicated card
	  * Click "Duplicate Card"
	  * Duplicated card will be created and shown on the main dashboard
  * List all Cards
	  * View the application
	  * All cards will be shown on the main dashboard automatically
   
#### 2. Card Modify Operations

+ Add a Text Visual Element to a Card
	- View the application
	- Choose a Card to modify from the main dashboard
	- Choose which page you want to add new visual element
	- Click "View" under the card description
	- Click "Add Text"
	- On the popup, fill up the necessary information (text, locationX, locationY, font name, font size, font type)
	- Click "Add Text"
	- Page will be updated with the new text visual element
+ Add an Image Visual Element to a Card
	- View the application
	- Choose a Card to modify from the main dashboard
	- Choose which page you want to add new visual element
	- Click "View" under the card description
	- Click "Add Image"
	- On the popup, upload the image by clicking "Choose File" and select the image from the computer
	- Fill up the necessary information (locationX, locationY, width, height)
	- Click "Add Image"
	- Page will be updated with the new image visual element
+ Delete a Visual Element from a Card
	- View the application
	- Choose a Card to modify from the main dashboard
	- Choose which page you want to delete visual element from
	- Click "View" under the card description
	- Click "Edit Text" or "Edit Image"
	- On the popup, choose the element you want to delete
	- Click "Delete"
	- Page will be updated with the new visual element deleted
+ Edit a Text Visual Element
	- View the application
	- Choose a Card to modify from the main dashboard
	- Choose which page you want to edit text visual element
	- Click "View" under the card description
	- Click "Edit Text"
	- On the popup, choose the text element you want to edit
	- Change the current information (text, locationX, locationY, font name, font size, font type)
	- Click "Edit Text"
	- Page will be updated with the edited text visual element
+ Edit an Image Visual Element
	- View the application
	- Choose a Card to modify from the main dashboard
	- Choose which page you want to edit image visual element
	- Click "View" under the card description
	- Click "Edit Image"
	- On the popup, choose the image element you want to edit
	- Change the current information (image, locationX, locationY, width, height)
	- Click "Edit Image"
	- Page will be updated with the edited image visual element
+ List all Images in S3 Buckets
	- View the application
	- Click "Show All Images"
	- On the popup, all images in S3 Buckets will be displayed
  
#### 3. Recipient Operations

+ Generate Link for Recipient
	- View the application
	- Choose a Card to generate link from the main dashboard
	- Click "Recipient" under the card description
	- A page with all 4 pages of the card will be shown
	- Copy the URL from the address bar of the browser
	- Recipient with this URL can view the card and all of its pages
