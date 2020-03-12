# eléncho - Unibz Timetable
![Application banner](img/app_banner.png)
## Motivation
In September 2019 I have started my first semester of Computer Science at the Free University of Bolzano and since the first days I was struggling with the online timetable. The main issue with the timetable was that it was slow, unreliable and very complex to use. Therefore after I few weeks I decided to take on a small side project which consisted of building an Android app to better manage my timetable. The main motivation that drove me to develop this app was that I wanted to explore new technologies and architectural patterns.
**Disclaimer: some architectural patterns used in this application are over-engineered for my use case, but the project was only for learning purpose.**
## The concept
At first I wanted to build a really simple one activity application however it mutated into something more really quick. As soon as I started developing the application I had into my mind a lot of different cool ideas, which then have seen form into the current version of the app (that still is under development).
## The architecture
The architecture adopted in this app is a mixture of ideas from the Clean Architecture and the Model-View-ViewModel. The main goal while developing this application was to create the business logic completely platform independent. I have tried to reduce as much as I could the number of Android related dependencies to further divide the dependencies between the framework and my logic.
![Architecture illustration](img/architecture_illustration.png)