const knex = require("knex")(require("../config/knexfile"));

const selectUserById = userId => knex("user").where("user_id", userId);

const selectUserByUsername = async username => {
  const result = await knex("user").where("username", username);
  if (result[0]) {
    return JSON.parse(JSON.stringify(result[0]));
  } else {
    return null;
  }
}

const createUser = async (userInfo) => {
  const result = await knex("user").insert({ ...userInfo });
  const user_id = result[0];
  return user_id;
};


const updateToken = (user_id, push_token) =>  knex("user").where("user_id", user_id).update({push_token});

module.exports = {
    selectUserById,
    selectUserByUsername,
    createUser,
    updateToken
  };