const admin = require("firebase-admin");
const serviceAccount = require("./../../protectme-firebase-adminsdk.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://protectme-6acf3.firebaseio.com",
});

const sendNotificationToContacts = (contactTokens, emergencyInfo, userInfo) => {
  const username = userInfo[0].firstname + " " + userInfo[0].surname;
  const payload = username + " has an emergency!"

   console.log(userInfo);
  var message = {
    data: {
      name: username,
      latitude: emergencyInfo.latitude,
      longitude: emergencyInfo.longitude,
    },
    tokens: contactTokens,
    notification: { body: payload},
  };

  admin
    .messaging()
    .sendMulticast(message)
    .then((response) => {
      //console.log("Success:", response);
    })
    .catch((error) => {
      //console.log("Error:", error);
    });
};

module.exports = {
  sendNotificationToContacts
};
