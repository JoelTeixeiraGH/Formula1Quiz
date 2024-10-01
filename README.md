# Sports Culture Application Development

This project involves the development of a sports culture application specifically related to Formula 1. The application will store a collection of scheduled and historical Formula 1 events, consulting APIs such as the Formula One API.

### Key Features

- **Event Storage:** The application will maintain a comprehensive database of past and upcoming Formula 1 events.
  
- **Periodic Challenges:** The app will issue periodic challenges to users whenever they are near a racetrack or during an active Formula 1 event (e.g., a Grand Prix). The frequency and number of challenges will depend on the event duration (e.g., 30 minutes or up to 3 days).

- **Dynamic Question Generation:** Questions for challenges will be sourced from the suggested APIs or similar, ensuring they are well-documented. All generated questions will be logged in a database, including whether they were answered, the provided answers, and any other relevant data to establish a sports IQ score (scoring system). A feature for generating random questions, irrespective of location and date, will also be included. Additionally, dynamically constructed questions using the APIs will be a bonus requirement.

- **Question Sharing via QR Codes:** Users will have the option to pass questions to other users (or devices) if they cannot answer them. This will be done by converting the question/answer into a QR code, which can be scanned by the recipient's device. The recipient will then store the question in their database and respond to the challenge.

- **Contextual Data Consideration:** To evaluate scores, the application will take into account various contextual factors, such as response time, weather conditions, altitude, etc. The OpenWeatherMap API will be utilized for contextual information. Data from other sources or web services may also be considered to enhance the application.

- **Event History Access:** Users will have access to a history of each Formula 1 event related to the proposed challenges. This history will include presented and answered questions, along with brief statistics and contextual data gathered.
