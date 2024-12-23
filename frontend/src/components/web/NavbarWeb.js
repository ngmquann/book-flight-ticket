import { Layout, Menu } from "antd"
import { Header } from "antd/es/layout/layout"
import React, { useEffect, useState } from "react"
import { Button, Modal, Checkbox, Form, Input, Flex } from "antd"
import { LockOutlined, UserOutlined } from "@ant-design/icons"
import { login } from "../../api/UserApi"
import Loading from "../../util/Loading"
import { jwtDecode } from "jwt-decode"
import logo from "../../assets/image/logo_flight.png"

const items1 = ["Chuyến bay", "Săn vé rẻ", "Blog du lịch"].map(
  (key, index) => ({
    key: index,
    label: key,
  })
)
export default function NavbarWeb() {
  const [open, setOpen] = useState(false)
  const showModal = () => {
    setOpen(true)
  }
  const handleOk = (e) => {
    console.log(e)
    setOpen(false)
  }
  const handleCancel = (e) => {
    console.log(e)
    setOpen(false)
  }
  const onFinish = async (values) => {
    const data = {
      email: values.username,
      password: values.password,
    }
    try {
      const result = await login(data)
      if (result) {
        const token = localStorage.getItem("jwtToken")
        if (token) {
          const userData = jwtDecode(token)
          if (userData && userData.role) {
            console.log(userData.role)
            if (userData.role === "ADMIN" || userData.role === "STAFF") {
              setTimeout(() => {
                window.location.href = "/admin/dashboard"
              }, 2000)
            } else {
              setTimeout(() => {
                window.location.href = "/"
              }, 2000)
            }
          }
        }
      }
    } catch (error) {
      console.error("Error:", error)
    }
  }

  return (
    <div>
      <Header
        style={{
          display: "flex",
          alignItems: "center",
        }}
      >
        <div className="demo-logo">
          <img src={logo} style={{ width: 50 }} />
        </div>
        <Menu
          theme="dark"
          mode="horizontal"
          defaultSelectedKeys={["0"]}
          items={items1}
          style={{
            flex: 1,
            minWidth: 0,
          }}
        />
        <div>
          <Button type="primary" onClick={showModal}>
            Login
          </Button>
          <Modal
            title="Login now!"
            open={open}
            onOk={handleOk}
            onCancel={handleCancel}
            footer={null}
          >
            <Form
              name="login"
              initialValues={{
                remember: true,
              }}
              style={{
                maxWidth: 360,
              }}
              onFinish={onFinish}
            >
              <Form.Item
                name="username"
                rules={[
                  {
                    required: true,
                    message: "Please input your Username!",
                  },
                ]}
              >
                <Input prefix={<UserOutlined />} placeholder="Username" />
              </Form.Item>
              <Form.Item
                name="password"
                rules={[
                  {
                    required: true,
                    message: "Please input your Password!",
                  },
                ]}
              >
                <Input
                  prefix={<LockOutlined />}
                  type="password"
                  placeholder="Password"
                />
              </Form.Item>
              <Form.Item>
                <Flex justify="space-between" align="center">
                  <Form.Item name="remember" valuePropName="checked" noStyle>
                    <Checkbox>Remember me</Checkbox>
                  </Form.Item>
                  <a href="">Forgot password</a>
                </Flex>
              </Form.Item>
              <Form.Item>
                <Button block type="primary" htmlType="submit">
                  Log in
                </Button>
                or <a href="">Register now!</a>
              </Form.Item>
            </Form>
          </Modal>
        </div>
      </Header>
    </div>
  )
}
