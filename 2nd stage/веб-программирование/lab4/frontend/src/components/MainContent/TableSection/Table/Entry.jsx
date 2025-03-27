import React from 'react';

const Entry = (props) => {
  const resultStyle = {
    color: props.result ? 'green' : 'purple',
  };

  return (
    <tr>
      <td>{props.x}</td>
      <td>{props.y}</td>
      <td>{props.r}</td>
      <td style={resultStyle}>{props.result ? "Попадание" : "Промах"}</td>
    </tr>
  );
}

export default Entry;
