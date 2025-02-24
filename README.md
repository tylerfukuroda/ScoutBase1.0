# ScoutBase1.0
Welcome to ScoutBase1.0! ScoutBase1.0 is the beta version of a football management system designed to help users view NFL Draft prospect data, create and update reports, and add players to their local database. This project was built using JavaFX 23 for the UI, uses SQLite to give users offline functionality, and leverages the DraftBase RESTful API (see DraftBase repo) to update the player database and to store user draft reports to our database. 

# Features
- **View Player Data:** ScoutBase fetches player data using the DraftBase API to get new players added to the database or to update existing player information. Player information will be saved locally so users may access player data while offline. Player information is presented in a table and users may use the search bar to search for a player.
- **Draft Reports:** Users may create, update, and delete draft reports for any draft prospect in the ScoutBase database. Reports will be stored locally for users and will be uploaded to the ScoutBase database via external API. As of right now, users may only view their draft reports. I am currently working on an update so users may view draft reports created by other users as long as the user is online. Users may also export their draft reports as a PDF.

**Login Page**

<img width="686" alt="Screenshot 2025-02-21 at 11 27 31 AM" src="https://github.com/user-attachments/assets/b52fd108-3f09-442f-8c69-5510fd3d4aa5" />

**Create an Account**

<img width="592" alt="Screenshot 2025-02-21 at 11 27 51 AM" src="https://github.com/user-attachments/assets/77a20c6a-6a0c-4032-b037-bf2a4ec60ae9" />

-Users are only allowed to use a 4-digit pin to access their account.

**Homepage**

<img width="1335" alt="Screenshot 2025-02-21 at 11 28 25 AM" src="https://github.com/user-attachments/assets/c78147f6-f560-4b1e-a8e6-caa22454de81" />

**Creating a Report**

<img width="590" alt="Screenshot 2025-02-21 at 11 28 36 AM" src="https://github.com/user-attachments/assets/7b1eb69b-6ce4-487f-841d-a006848456dd" />

**Report Menu**

<img width="794" alt="Screenshot 2025-02-21 at 11 29 08 AM" src="https://github.com/user-attachments/assets/431c4670-873f-4442-9b75-d8e1626323d7" />

**Viewing a Report**

<img width="593" alt="Screenshot 2025-02-21 at 11 29 17 AM" src="https://github.com/user-attachments/assets/eaf999c2-6e18-447f-8965-7e4673afeb5b" />

-Users have the option to edit, export, or delete their report.

**Exporting Report to PDF**
<img width="593" alt="Screenshot 2025-02-21 at 11 29 26 AM" src="https://github.com/user-attachments/assets/899dd0d6-4600-4660-adbe-84c6a737a5df" />

**Create A Player**
<img width="494" alt="Screenshot 2025-02-21 at 11 29 39 AM" src="https://github.com/user-attachments/assets/6648f89f-5deb-4f56-b64e-6842d165e047" />
