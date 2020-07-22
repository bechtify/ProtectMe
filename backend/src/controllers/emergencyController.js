const emergencyService = require("../services/emergencyService");

// Handle request for POST /emergencies/:userId
const postEmergency = async (req, res) => {
  try {
    const userId = parseInt(req.params.userId);
    const emergencyInfo = req.body;
    let result = await emergencyService.postEmergency(emergencyInfo, userId);
    if (result) {
      res.sendStatus(200);
    } else {
      res.status(400).json("Emergency not added!");
    }
  } catch (e) {
    res.status(400).send({ error: e });
  }
};


const _call_emergency_contacts = async (emergencyInfo, userId) => {

}

module.exports = {
  postEmergency,
};
