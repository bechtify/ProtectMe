const userStorage = require("./../storage/userStorage");
const bcrypt = require("bcryptjs");

// Service for GET /users/:userId
const selectUserById = async (userId) => {
  const unparsedData = await userStorage.selectUserById(userId);
  const results = JSON.parse(JSON.stringify(unparsedData[0]));
  delete results.password;
  return results;
};

// Service for login and register to test existing user via email
const selectUserByUsername = username => {
  return userStorage.selectUserByUsername(username);
};

// Service for POST /user/register
// check for already existing username and then create user
const registerUser = async (userInfo) => {
  const alreadyExists = await selectUserByUsername(
    userInfo.username
  );
  if (alreadyExists) {
    return Promise.reject("Username already taken");
  } else {
    // create a hashed password to store in database
    const hashedPassword = await bcrypt.hash(userInfo.password, 10); //  10 salt rounds
    userInfo.password = hashedPassword;
    const user_id = await userStorage.createUser(userInfo);
    return Promise.resolve(user_id);
  }
};

// service for POST /user/login
const logInUser = async (username, password, push_token) => {
  const user = await selectUserByUsername(username);
  if (!user) {
    return Promise.reject("Unable to login!");
  }
  // Compare hashed passwords here
  const isMatch = await bcrypt.compare(password, user.password);
  if (!isMatch) {
    return Promise.reject("Unable to login!");
  }
  await userStorage.updateToken(user.user_id, push_token);
  return Promise.resolve(user.user_id);
};

module.exports = {
  selectUserById,
  selectUserByUsername,
  registerUser,
  logInUser
};
