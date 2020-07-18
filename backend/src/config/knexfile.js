const { dbConfig } = require("./env");

// configure knex with information from env.js file
module.exports = {
  client: "mysql",
  connection: {
    ...dbConfig
  },
  pool: { min: 1, max: 100 }
};
