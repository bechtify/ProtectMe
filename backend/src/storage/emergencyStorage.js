const knex = require("knex")(require("../config/knexfile"));

const postEmergency = async (emergencyInfo, user_id) => {
  const result = await knex("emergency").insert({ ...emergencyInfo });
  const emergency_id = result[0];
  await knex("user_emergencies").insert({ user_id, emergency_id });
    return emergency_id;
};

const getEmergencyContactTokens = async (usernames) =>
  knex("user").whereIn("username", usernames).select("push_token");

  const getUserInfo = async (userId) =>
  knex("user").where("user_id", userId).select("firstname", "surname");


const selectContactNamesByUserId = (userId) =>
  knex("contact as c")
    .join("user_contacts", "c.contact_id", "=", "user_contacts.contact_id")
    .where("user_contacts.user_id", userId)
    .distinct("c.username");

module.exports = {
  postEmergency,
  getEmergencyContactTokens,
  selectContactNamesByUserId,
  getUserInfo
};
