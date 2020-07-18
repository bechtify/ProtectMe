const userService = require("../services/userService");

// Handle request for GET /users/:userId
// If user is requesting himself, he will get more information such as email etc.
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

// User will be signed up and gets a token generated
const registerUser = async (req, res) => {
  try {
    const userImage = req.file;
    const newUser = await authService.signUpUser(req.body, userImage);
    const token = _generateAuthToken(newUser);
    res.status(201).json(token);
  } catch (e) {
    res.status(400).send(e);
  }
};

// User will be logged in and gets a token generated
const logInUser = async (req, res, next) => {
  try {
    const user = await authService.logInUser(req.body.email, req.body.password);
    const token = _generateAuthToken(user);
    res.send({ token });
  } catch (e) {
    res.status(400).send(e);
  }
};

module.exports = {
  getUser,
  registerUser,
  logInUser,
};
