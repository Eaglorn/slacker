const sqlite3 = require('sqlite3').verbose()
const { open } = require('sqlite')

const request = async function () {
  const db = await open({
    filename: './database.db',
    driver: sqlite3.verbose().Database
  })
  await db.exec('VACUUM')
  db.close()
}

request()
  .then(() => {})
  .catch((err) => {
    console.log(err)
  })
