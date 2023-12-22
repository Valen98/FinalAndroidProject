README: FINAL PROJECT 
Leo wahlandt (DWAHLANDT23@UBishops.ca)
Michael Bielawski (MBIELAWSKI21@Ubishops.ca)

Github Repo Link: https://github.com/Valen98/FinalAndroidProject

Authentication issues:
One of the main issues we ran into was testing the apps in different areas. When on Campus, both of us had no issue with accessing and updating the database but when connected to a different network, we would get multiple issues with authentication tokens for Firebase not being properly recognized by Android Studio. Please keep in mind when testing on your own device that this may be a potential issue. We noticed it specifically effected the login process, as the creation of a new account would not register on the database and not allow a user to login with the new credentials, or any existing credentials. We could not figure out how to mitigate this issue. 

Project downfalls: 

Timer for simulating posts by users:
We decided not to implement the Timer for simulating posts. We ran out of time while trying to get the app to function somewhat properly and getting the basic functionality down. We were able to get the post feed to upload the current collection of posts (see demo video after post creation) but we’re unsure if it will do so passively.

To implement this, we would of setup a series of “fake” posts that hold and image and title and create a loop to randomly assign users already in the database to a random image from maybe 2 or 3 images and 2 or 3 titles. Then the feed would be updated, and you could watch the posts come in, in real time. 

User Personal Feed:
We did not implement a personal feed for each user. We had a hard enough time getting the feed to function and manage the different aspects of the database to populate for the current user. 

To implement this, we would have copied the feed implementation and changed the parameters to only include posts with a matching username or userID. This could have also copied the layout of the profile page for the current user and have a lazy grid of posts in the bottom section. 

“Like” feature for a post:
We did not add a like button for the posts that do get posted to the feed. 

To implement this, we could have added another database object for the posts themselves with a “Likes” section. Then in the post feed we could have implemented a button for “liking” and update the database for each post and increment the like by +1 for every press of the like button. The button would be a toggle button and the database would be able to add and subtract likes based on the toggle of the button. 

Following Suggested Users:
We did not add a button to follow suggested users.

To implement this, we could have created a “card” for each suggested user that also housed a follow button. The database would also have a section for the number of users you are following and could have kept track so that on your profile page you can see how many users you are following. Then you could have the card removed from the suggested section once you press the follow button to mimic the Instagram functionality of this section. 

Image from Gallery for Profile pictures: 
We did not allow for selection of images from the gallery.

To implement this, we could have added access to the android device gallery and allowed users to select an image that is housed on the device. This could have done through a dedicated button on the profile page. 


Post Project review: 
This project was both challenging and fun to do. When we were able to get a functionality, it felt really good, and it was fun to see the app come together as we progressed. We started with the Login process first and worked into the app from there. In retrospect, we would have maybe organized our database more efficiently so that navigation and getting certain information wasn’t so difficult. We also would have implemented the items that we missed mentioned above so that it felt more complete. 

From the both of us, thank you for a rewarding and productive semester. We wish you a Happy holiday and Happy new year!
