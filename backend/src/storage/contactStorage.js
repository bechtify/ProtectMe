const knex = require("knex")(require("../config/knexfile"));

const selectContactsByUserId = (userId) =>
  knex("contact as c")
    .join("user_contacts", "c.contact_id", "=", "user_contacts.contact_id")
    .where("user_contacts.user_id", userId)
    .select(
      "c.contact_id",
      "c.firstname",
      "c.surname",
      "c.display_name",
      "c.phone",
      "c.address",
      "c.relationship"
    );

//SELECT * FROM contact JOIN user_contacts ON contact.contact_id = user_contacts.contact_id WHERE user_contacts.user_id = "1";

const selectContactById = (contactId) =>
  knex("contact").where("contact_id", contactId);

module.exports = {
  selectContactsByUserId,
  selectContactById,
};
