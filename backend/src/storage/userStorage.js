const knex = require("knex")(require("../config/knexfile"));

const selectUserById = userId => knex("user").where("user_id", userId);

module.exports = {
    selectUserById
  };