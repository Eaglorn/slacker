import { ChangeEvent, useState } from 'react'

function Forms(): JSX.Element {
  const [name, setName] = useState<string>('')

  const onChangeInputName = (event: ChangeEvent<HTMLInputElement>) => {
    setName(event.target.value)
  }

  return (
    <div className="container mx-auto bg-red-700 w-96 h-52">
      <input type="text" value={name} onChange={onChangeInputName} className="text-black" />
    </div>
  )
}

export default Forms
