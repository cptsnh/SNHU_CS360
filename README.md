# SNHU_CS360
## CS360 Mobile Architecture and Programming

### Briefly summarize the requirements and goals of the app you developed. What user needs was this app designed to address?  
<< need to finish >>  
  
### What screens and features were necessary to support user needs and produce a user-centered UI for the app? How did your UI designs keep users in mind? Why were your designs successful?  
As I learned throughout the course, it makes sense to consolidate tasks into activities and run them using fragments. This makes the process of using the app much easier and pleasant for the customer because they don’t have to traverse many different screens to accomplish a goal. The main path the customer traverses in this app is logging in, and then adding, deleting, or editing an item in the list. I accomplished this with one activity. That activity contains a fragment for each of the tasks listed earlier. There are additional actions that a customer can perform, but they are activities or fragments that branch from the main path. I feel like this helps maintains focus on the main goal of the app for the customer.  
  
### How did you approach the process of coding your app? What techniques or strategies did you use? How could those be applied in the future?  
My process for coding the app was primarily Agile. The first major thing I did was programmed the navigation between fragments to represent the major pathway the customer traverse to accomplish the primary goal of the app. I then went back and completed the UI on each activity and fragment to add in the views, program error checking, and establishing communication channels. After that was done, I went back and worked on the themes, styles, and colors of the app.  
  
### How did you test to ensure your code was functional? Why is this process important and what did it reveal?  
Testing involved using the Android Studio test packages, as well as the manual process of setting breakpoints and analyzing the state of events and variables. The process of analyzing breakpoints was extremely helpful in our assignment with sensors, and they were also applicable in this project. I was passing information between fragments using SafeArgs and serialized classes. Setting breakpoints enabled me to see value associated with variables as messaged traversed the app.  

### Considering the full app design and development process, from initial planning to finalization, where did you have to innovate to overcome a challenge?  
This was the first time I have developed an app using mobile technology. It was a steep learning curve because I had to learn Android Studio IDE, as well as how to program a UI using the Android programming framework. One challenge I had to overcome was combining learning the IDE with the development of the project itself since we had specific deadlines for P1, P2, and P3.  
So, my process for that was divide-and-conquer by building small projects with certain functionalities, and then adding those into the final project as needed. One of the benefits of using fragments is that they are kind of like plug-and-play. When I got one working in a prototype app, I knew I could plug it in to my final project.  

### In what specific component from your mobile app were you particularly successful in demonstrating your knowledge, skills, and experience?  
The component I am most proud of was the database implementation and hooking it in with the RecyclerView list elements. I designed the database so that it was relational between the tables. I wanted it designed so that each account had its own database. So, the item table had a foreign key referencing a primary key of the customer table.
In the application, when an item is modified, added, or deleted, the customer object is sent as a serialized argument to the appropriate fragment so that the customerId field can be referenced. After the item is modified, the change is reflected to the backing database to the appropriate record which links to the respective customer’s item list.
