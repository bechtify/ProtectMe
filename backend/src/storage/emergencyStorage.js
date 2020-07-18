const knex = require("knex")(require("../config/knexfile"));

const postEmergency = async (emergencyInfo, user_id) => {
  const result = await knex("emergency").insert({ ...emergencyInfo });
  const emergency_id = result[0];
  await knex("user_emergencies").insert({ user_id, emergency_id });
  return emergency_id;
};

module.exports = {
  postEmergency,
};
