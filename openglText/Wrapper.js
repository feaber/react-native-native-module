import React, { Component } from 'react';
import MyGLBox from 'react-native-opengl-native';
import {
  View,
  Text,
  Button,
  TextInput
} from 'react-native';

class Wrapper extends Component {
  constructor(props) {
    super(props)
    this.state = {
      message: 'Hello World!!'
    }
  }

  handleClick() {
    const { newMessage } = this.state

    this.setState({
      message: newMessage,
      newMessage: ''
    })
  }

  handleChange(text) {
    this.setState({
      newMessage: text
    })
  }

  render() {
    const { message, newMessage } = this.state

    return (
      <View style={{ flex: 1 }}>
        <View style={{
          flex: 1,
          justifyContent: 'space-around',
          alignItems: 'center'
        }}>
          <Text style={{ fontSize: 20 }}>Insert new text</Text>
          <TextInput
            style={{ width: '80%', height: 40, borderColor: 'gray', borderWidth: 1 }}
            onChangeText={text => this.handleChange(text)}
            value={newMessage}
          />
          <Button
            title="Change text"
            onPress={() => this.handleClick()}
          />
        </View>
        <View style={{ flex: 2, width: '100%', height: '100%', overflow: 'hidden' }}>
          <MyGLBox
            style={{ flex: 1, width: '100%', height: '100%' }}
            text={message}
          />
        </View>
      </View>
    )
  }
}

export default Wrapper;
