const userService = require("../services/userService");

// Handle request for GET /users/:userId
const getUser = async (req, res) => {
  try {
    const userId = parseInt(req.params.userId);
    //console.log("Controller:"+userId);
    let result = await userService.selectUserById(userId);
    if (result) {
      res.send(result);
    } else {
      res.send("User not found!");
    }
  } catch (e) {
    res.send({ error: e });
  }
};

// Handle request for POST /users/register
const registerUser = async (req, res) => {
  try {
    const userInfo = req.body;
    let result = await userService.registerUser(userInfo);
    if (result) {
      res.json(result);
    } else {
      res.send("User not created!");
    }
  } catch (e) {
    res.send({ error: e });
  }
};

// Handle request for POST /users/login
const logInUser = async (req, res, next) => {
  try {
    const credentials = req.body;
    let result = await userService.logInUser(credentials.username, credentials.password);
    if (result) {
      res.json(result);
    } else {
      res.send("User not logged in!");
    }
  } catch (e) {
    res.send({ error: e });
  }
};

module.exports = {
  getUser,
  registerUser,
  logInUser,
};
