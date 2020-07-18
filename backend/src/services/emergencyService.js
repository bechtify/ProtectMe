const emergencyStorage = require("./../storage/emergencyStorage");

// Service for POST /emergency/:userId
const postEmergency = async (emergencyInfo, userId) => {
  try {
    const newEmergencyId = await emergencyStorage.postEmergency(
      emergencyInfo,
      userId
    );
    return newEmergencyId;
  } catch (e) {
    return e;
  }
};

module.exports = {
  postEmergency
};
