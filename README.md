# ProgressTracking
_A solution to help you keep track of your studies._

## What is ProgressTracking?
As the name suggests, it's an application designed to assist you in tracking the progress of the courses you are taking. The software acts as middleware between Trello and an online learning platform. You can search for a course and send it to Trello as a card with checklists. Each card will contain one checklist for each module.

## How does the application work?
While the application is currently in development, the intended workflow is as follows: the user will enter the course name, and the application will conduct a search. A list of courses will be returned, allowing the user to select the desired one. Once selected, the application will create a card on a Trello board.

## Why custom wrappers and API execution class?
Although there are libraries that provide wrappers for certain APIs and tools for making HTTP requests (such as Retrofit and OkHttp), my decision to create custom wrappers and tools was motivated by the desire to tackle additional challenges, thereby enhancing my development skills. Building these components from the ground up will provide me with a deeper understanding of how these tools work and contribute to my knowledge of project architecture. My aim is to tailor them to the specific needs of ProgressTracking while further honing my development skills.

