const contactStorage = require("./../storage/contactStorage");

// Service for GET /contacts/user/:userId
const selectContactsByUserId = async (userId) => {
  const unparsedData = await contactStorage.selectContactsByUserId(userId);
  const results = JSON.parse(JSON.stringify(unparsedData));
  return results;
};

// Service for GET /contacts/:contactId
const selectContactById = async (contactId) => {
  const unparsedData = await contactStorage.selectContactById(contactId);
  const results = JSON.parse(JSON.stringify(unparsedData[0]));
  return results;
};

// Service for POST /contacts/:userId
const addContact = async (contactInfo, userId) => {
  try {
    const newContactId = await contactStorage.addContact(contactInfo, userId);
    const newContact = await selectContactById(newContactId);
    return newContact;
  } catch (e) {
    return e;
  }
};

// Service for PUT /contacts/:contactId
const editContact = async (contactInfo, contactId) => {
  await contactStorage.editContact(contactInfo, contactId);
};

// Service for DELETE /contacts/:userId
const deleteContacts = async (contactIds) => {
  return contactStorage.deleteContacts(contactIds);
};

module.exports = {
  selectContactsByUserId,
  selectContactById,
  addContact,
  editContact,
  deleteContacts,
};
