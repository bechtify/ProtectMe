const knex = require("knex")(require("../config/knexfile"));

const selectContactsByUserId = (userId) =>
  knex("contact as c")
    .join("user_contacts", "c.contact_id", "=", "user_contacts.contact_id")
    .where("user_contacts.user_id", userId)
    .select(
      "c.contact_id",
      "c.username",
      "c.display_name",
      "c.phone",
      "c.address",
      "c.relationship"
    );

//SELECT * FROM contact JOIN user_contacts ON contact.contact_id = user_contacts.contact_id WHERE user_contacts.user_id = "1";

const selectContactById = (contactId) =>
  knex("contact").where("contact_id", contactId);

const addContact = async (contactInfo, user_id) => {
  const result = await knex("contact").insert({ ...contactInfo });
  const contact_id = result[0];
  await knex("user_contacts").insert({ user_id, contact_id });
  return contact_id;
};

const editContact = (contactInfo, contactId) =>
  knex("contact")
    .update({ ...contactInfo })
    .where("contact_id", contactId);

const deleteContacts = async (contactIds) => {
  console.log("Storage");
  const result = await knex("contact").del().whereIn("contact_id", contactIds);
  return result;
};

module.exports = {
  selectContactsByUserId,
  selectContactById,
  addContact,
  editContact,
  deleteContacts,
};
