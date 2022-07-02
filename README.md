# WalletAndroidApp 📝💵
 <p align="center">
 <img  src="https://media.giphy.com/media/HhnLVOWdRw4nspwJ7z/giphy.gif" width="350" height="350"/></p>
 </p>  
The `Wallet` is a full-stack application that gives people the opportunity to control their money flow by introducing and keeping track of their expenses and incomes. 

## Motivation 💡🤔
Money is an integral part of the life of almost every person. After the research and gaining personal experience, I realized that saving money is one of the hardest skills. In addition to that, it is a well-known fact, that not all buying decisions are made consciously, some of them are made because of our mood or temporary desire for a specific thing. Something that can mitigate the consequences of the external influence of purchasing decisions and prevent unnecessary and unplanned spending is keeping track of your expenses and incomes. The goal was to create an application, that would be simple in use, yet satisfy the user’s needs and provide the necessary functionality for budget planning and tracking.

## Tech Stack 💻📱
* Android Jetpack Compose
* Java Spring
* MySQL
* REST API

 <p align="center">
 <img  src="https://user-images.githubusercontent.com/57729718/177005163-e53e1c4e-99fe-4244-9046-64023cf8ab90.png" width="1000" /></p>
 </p>  
 
 ## System architecture ⚙️
The `Wallet` is a full-stack application that is based on the client-server architecture. The diagram below demonstrates the high-level architecture of the system. 

<p align="center">
 <img  src="https://user-images.githubusercontent.com/57729718/177005358-e9452fd0-4cd8-43fd-981f-a6af396456d0.png" width="1000" /></p>
 </p>  
 
 This architecture fully meets the application requirements and will provide efficient operation. Each component is isolated and independent from the other, giving the system the necessary level of maintainability. All the system components have their responsibilities.
 
 ## User stories (What the app does)
 <p align="center">
 <img  src="https://user-images.githubusercontent.com/57729718/177005577-d71def14-d79a-49a3-bccd-704ec318110e.gif" width="500" /></p>
 </p>  
 
- [x] User can register and login to the application     
- [x] User can see the list of all transactions he entered into the application, as well as data about his total income, expense and budget    
- [x] User can filter transactions by time and sort them by different parameters (location, date, wallet, category, etc.)        
- [x] User can add the records to the application      
- [x] User can see, edit and delete his records
- [x] User can generate the in app reports about his money flow
- [x] Wallet should be maintainable and scalable
- [x] Wallet should be responsive and secure the data

## Client side structure 🏗️🏛️
* The limitation of mobile devices resources and hindered vertical scalability were also taken into the consideration during the comparison of different possible solutions. 
* The separation of concerns principle is crucial since the processes of your application are under the control of the Operating System and due to the resource’s limitation Android OS may kill some of the processes for optimization. T
* UI should contain as less logic, as possible and should be driven by the data. Thereby, the interface will be much more responsive, and the user experience will be improved.

<h3 align = "center">
To follow the best practices above the MVVM design pattern was chosen for this project.
</h3>


<p align="center">
 <img  src="https://user-images.githubusercontent.com/57729718/177005857-8f7bccb6-0fcf-426c-8ce6-a84f377f673a.png" width="1000" /></p>
 </p>
 
 ## DEMO 💪🎇
 <h3 align = "center">
It is hard to show the whole app's potential, so only some of the functionalities are included 🙂
</h3>

<img src="https://user-images.githubusercontent.com/57729718/177006598-52743b7b-fe2f-46d4-b7a2-40850dd7b271.gif" width="30%"></img> <img src="https://user-images.githubusercontent.com/57729718/177006608-19b398a5-17c7-44ec-bf48-a207f0f783c0.gif" width="30%"></img> <img src="https://user-images.githubusercontent.com/57729718/177006609-0184f2b7-d002-4db0-af58-e28b435df2eb.gif" width="30%"></img> 



