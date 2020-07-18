const userStorage = require("./../storage/userStorage");
//const utils = require("./../utils/utils");
////const bcrypt = require("bcryptjs");

// Service for GET /users/:userId
// In case user is requesting himself
const selectUserById = async userId => {
    console.log("Service:"+userId);
  const unparsedData = await userStorage.selectUserById(userId);
  const results = JSON.parse(JSON.stringify(unparsedData[0]));
  delete results.password;
  return results;
};



module.exports = {
  selectUserById
};
