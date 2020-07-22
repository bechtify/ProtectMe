const userService = require("../services/userService");

// Handle request for GET /users/:userId
const getUser = async (req, res) => {
  try {
    const userId = parseInt(req.params.userId);
    let result = await userService.selectUserById(userId);
    if (result) {
      res.status(200).json(result);
    } else {
      res.status(404).json("User not found!");
    }
  } catch (e) {
    res.status(400).send({ error: e });
  }
};

// Handle request for POST /users/register
const registerUser = async (req, res) => {
  try {
    const userInfo = req.body;
    let result = await userService.registerUser(userInfo);
    if (result) {
      res.status(200).json(result);
    } else {
      res.status(400).json("User not registered!");
    }
  } catch (e) {
    res.status(400).send({ error: e });
  }
};

// Handle request for POST /users/login
const logInUser = async (req, res, next) => {
  try {
    const credentials = req.body;
    let result = await userService.logInUser(credentials.username, credentials.password, credentials.push_token);
    if (result) {
      res.status(200).json(result);
    } else {
      res.status(400).json("User not logged in!");
    }
  } catch (e) {
    res.status(400).send({ error: e });
  }
};

module.exports = {
  getUser,
  registerUser,
  logInUser,
};
