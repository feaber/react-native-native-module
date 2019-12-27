import { Component } from 'react';
import OpenglNative from 'react-native-opengl-native';


class ModuleWrapper extends Component {
  constructor (props) {
    super(props)
    this.state = {
      results: ''
    }
  }

  componentDidMount () {
    OpenglNative.sampleMethod('Konrad', 777, r => {
      this.setState({
        results: `Yess! - ${r}`
      })
    })
  }

  render () {
    const { results } = this.state
    const output = `Test123 - ${results}`

    console.log(output)
    return output
  }
}

export default ModuleWrapper
