const userService = require("../services/userService");

// Handle request for POST /users/rating
// With body {litId: number, typeId: number, value: number, ratedUserId: number}
const postEmergency = async (req, res) => {
    try {
        const userId = parseInt(req.params.userId);
      await userService.postEmergency(req.body, userId);
      res.send();
    } catch (e) {
      res.send({ error: e });
    }
  };

  module.exports = {
    postEmergency
  };