const emergencyService = require("../services/emergencyService");

// Handle request for POST /users/rating
// With body {litId: number, typeId: number, value: number, ratedUserId: number}
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

module.exports = {
  postEmergency,
};
