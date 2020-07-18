const userService = require("../services/userService");

// Handle request for GET /users/:userId
// If user is requesting himself, he will get more information such as email etc.
const getUsers = async (req, res) => {
  try {
    const userId = parseInt(req.params.userId);
    console.log("Controller:"+userId);
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

// Handle request for GET /users/me
// Almost the same as /users/:userId
//This here is called for editing own profile
const getMe = async (req, res) => {
  try {
    const result = await userService.selectUserById(req.user.id);
    if (result) {
      res.send(result);
    } else {
      res.send("User not found!");
    }
  } catch (e) {
    res.send({ error: e });
  }
};

// Handle request for PUT /users/me --> Update user information
// User picture is parsed via multer as a middleware
// With user information as body --> see userService
const updateMe = async (req, res) => {
  try {
    const userInfo = req.body;
    const userImage = req.file;
    delete userInfo.password;
    await userService.updateUser(req.user, userInfo, userImage);
    res.send();
  } catch (e) {
    res.send({ error: e });
  }
};

// Handle request for POST /users/rating
// With body {litId: number, typeId: number, value: number, ratedUserId: number}
const postRating = async (req, res) => {
  try {
    await userService.rateUser(req.body, req.user.id);
    res.send();
  } catch (e) {
    res.send({ error: e });
  }
};

module.exports = {
  getUsers,
  getMe,
  updateMe,
  postRating
};
