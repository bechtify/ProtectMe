const emergencyStorage = require("./../storage/emergencyStorage");
const utils = require("./../utils/send_notification");

// Service for POST /emergency/:userId
const postEmergency = async (emergencyInfo, userId) => {
  try {
    const newEmergencyId = await emergencyStorage.postEmergency(
      emergencyInfo,
      userId
    );
    const contactUsernames = JSON.parse(JSON.stringify(await emergencyStorage.selectContactNamesByUserId(userId))).map(username => {
      return username.username;
    });
    const contactTokens = JSON.parse(JSON.stringify(await emergencyStorage.getEmergencyContactTokens(contactUsernames))).map(token => {
      return token.push_token;
    });

    const userInfo = JSON.parse(JSON.stringify(await emergencyStorage.getUserInfo(userId)));
    utils.sendNotificationToContacts(contactTokens, emergencyInfo, userInfo);
    return newEmergencyId;
  } catch (e) {
    return e;
  }
};

module.exports = {
  postEmergency
};
