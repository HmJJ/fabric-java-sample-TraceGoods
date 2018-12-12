package main

import (
	"fmt"
	"strconv"
	"encoding/json"
	"math"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

// SimpleChaincode example simple Chaincode implementation
type SimpleChaincode struct {
}

type Role struct {
	Id string
	PlayerName string
	Password string
	RoleType string
	RoleName string
	BasisHealth int
	RealHealth int
	BasisAttack int
	RealAttack int
	Money int
	Exp int
	Level int
}


var avengers = map[int]string{}
var deedpools = map[int]string{}
var doctors = map[int]string{}
var blacksmiths = map[int]string{}
var roles int

func (t *SimpleChaincode) Init(stub shim.ChaincodeStubInterface) pb.Response {
	fmt.Println("the_dragon Init")
	var data [5]string
	roles = 0
	var id string
	//add avenger
	id = strconv.Itoa(roles+1)
	avenger_NPC_01 := Role{Id:id, PlayerName:"nott", Password:"123456", RoleName:"avenger_NPC_01", RoleType:"avenger",BasisHealth:100,RealHealth:100,BasisAttack:15,RealAttack:15,Money:50,Exp:0,Level:1}
	avenger_NPC_01_Bytes, _ := json.Marshal(avenger_NPC_01)
	stub.PutState(id, avenger_NPC_01_Bytes)
	avengers[len(avengers)]=id
	roles = roles+1
	//add deedpool
	id = strconv.Itoa(roles+1)
	deedpool_NPC_01 := Role{Id:id, PlayerName:"nott", Password:"123456", RoleName:"deedpool_NPC_01", RoleType:"deedpool",BasisHealth:50,RealHealth:50,BasisAttack:5,RealAttack:5,Money:50,Exp:5,Level:1}
	deedpool_NPC_01_bytes, _ := json.Marshal(deedpool_NPC_01)
	stub.PutState(id, deedpool_NPC_01_bytes)
	deedpools[len(deedpools)]=id
	roles = roles+1
	//add doctor
	id = strconv.Itoa(roles+1)
	doctor_NPC_01 := Role{Id:id, PlayerName:"nott", Password:"123456", RoleName:"doctor_NPC_01", RoleType:"doctor",BasisHealth:9999999,RealHealth:9999999,BasisAttack:999,RealAttack:999,Money:999,Exp:999,Level:999}
	doctor_NPC_01_bytes, _ := json.Marshal(doctor_NPC_01)
	stub.PutState(id, doctor_NPC_01_bytes)
	doctors[len(doctors)]=id
	roles = roles+1
	//add blacksmith
	id = strconv.Itoa(roles+1)
	blacksmith_NPC_01 := Role{Id:id, PlayerName:"nott", Password:"123456", RoleName:"blacksmith_NPC_01", RoleType:"blacksmith",BasisHealth:1000,RealHealth:1000,BasisAttack:9999999,RealAttack:9999999,Money:999,Exp:999,Level:999}
	blacksmith_NPC_01_bytes,_ := json.Marshal(blacksmith_NPC_01)
	stub.PutState(id, blacksmith_NPC_01_bytes)
	blacksmiths[len(blacksmiths)]=id
	roles = roles+1

	data[0] = "初始化成功"
	data[1] = string(avenger_NPC_01_Bytes)
	data[1] = string(deedpool_NPC_01_bytes)
	data[1] = string(doctor_NPC_01_bytes)
	data[1] = string(blacksmith_NPC_01_bytes)
	databytes,_ := json.Marshal(data)
	return shim.Success(databytes)
}

func (t *SimpleChaincode) Invoke(stub shim.ChaincodeStubInterface) pb.Response {
	fmt.Println("the_dragon Invoke")
	function, args := stub.GetFunctionAndParameters()
	if function == "invoke" {
		// Make payment of X units from A to B
		return t.invoke(stub, args)
	} else if function == "delete" {
		// Deletes an entity from its state
		return t.delete(stub, args)
	} else if function == "query" {
		// the old "Query" is now implemtned in invoke
		return t.query(stub, args)
	} else if function == "create" {
		return  t.createRole(stub, args)
	} else if function == "cure" {
		return  t.cure(stub, args)
	} else if function == "repair" {
		return  t.repair(stub, args)
	} else if function == "fight" {
		return  t.fight(stub, args)
	} else if function == "rebirth" {
		return  t.rebirth(stub, args)
	}

	return shim.Error("Invalid invoke function name."+ function + "Expecting \"invoke\" \"delete\" \"query\" \"create\" \"cure\" \"repair\" \"fight\" \"rebirth\" ")
}

func (t *SimpleChaincode)createRole(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	//var role Role    // Entities
	var id, playername, password, roleType, rolename string
	var health, attack, money, exp, level int // Asset holdings
	var err error


	// Initialize the chaincode
	id = strconv.Itoa(roles+1)
	playername = args[0]
	password = args[1]
	roleType = args[2]
	rolename = args[3]

	health, err = strconv.Atoi(args[4])
	if err != nil {
		return shim.Error("Expecting integer value for asset holding")
	}
	attack, err = strconv.Atoi(args[5])
	if err != nil {
		return shim.Error("Expecting integer value for asset holding")
	}
	money, err = strconv.Atoi(args[6])
	if err != nil {
		return shim.Error("Expecting integer value for asset holding")
	}
	exp, err = strconv.Atoi(args[7])
	if err != nil {
		return shim.Error("Expecting integer value for asset holding")
	}
	level, err = strconv.Atoi(args[8])
	if err != nil {
		return shim.Error("Expecting integer value for asset holding")
	}
	fmt.Printf("id = %d,playername=%s, password = %s, rolename = %s, roletype=%s, health= %d, attack= %d, money= %d, exp= %d, level= %d\n", id, playername, password, rolename, roleType, health, attack, money, exp, level)

	// Write the state to the ledger
	role := Role{Id:id, Password:password, PlayerName:playername, RoleType:roleType, RoleName:rolename, BasisHealth:health, RealHealth:health, BasisAttack:attack, RealAttack:attack, Money:money, Exp:exp, Level:level}
	rolebytes,_ := json.Marshal(role)
	err = stub.PutState(id, rolebytes)
	if err != nil {
		return shim.Error(err.Error())
	}

	roles = roles+1
	switch roleType {
	case "avenger":
		avengers[len(avengers)] = id
	case "deedpool":
		deedpools[len(deedpools)] = id
	case "doctor":
		doctors[len(doctors)] = id
	case "blacksmith":
		blacksmiths[len(blacksmiths)] = id
	}

	var data [2]string
	data[0] = "创建角色成功"
	data[1] = string(rolebytes)
	databytes,_ := json.Marshal(data)
	return shim.Success(databytes)
}

func (t *SimpleChaincode) invoke(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	
	return shim.Success(nil)
}


//战斗
func (t *SimpleChaincode) fight(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	Attack_from := Role{}
	Attack_to := Role{}
	var id_from, id_to string
	var err error
	var flag int //0:击败对手  1:被反杀

	id_from = args[0]
	id_to = args[1]

	// Get the Attack_from's state from the ledger
	frombytes, err1 := stub.GetState(id_from)
	if err1 != nil {
		return shim.Error("Failed to get state")
	}
	if frombytes == nil {
		return shim.Error("Entity not found")
	}
	err = json.Unmarshal(frombytes, &Attack_from)

	// Get the Attack_from's state from the ledger
	tobytes, err2 := stub.GetState(id_to)
	if err2 != nil {
		return shim.Error("Failed to get state")
	}
	if tobytes == nil {
		return shim.Error("Entity not found")
	}
	err = json.Unmarshal(tobytes, &Attack_to)

	if Attack_from.RealHealth <= 0 {
		return shim.Error("You can't attack it because you are already dead.")
	} else if Attack_to.RealHealth <= 0 {
		return shim.Error("You can't attack it because it's already dead.")
	} else {
		Attack_to.RealHealth = Attack_to.RealHealth - Attack_from.RealAttack
		if Attack_from.RealAttack > 7 {
			Attack_from.RealAttack = Attack_from.RealAttack - 5
		}else {
			Attack_from.RealAttack = 2
		}
		if Attack_to.RealHealth > 0 {
			Attack_from.RealHealth = Attack_from.RealHealth - Attack_to.RealAttack
			if Attack_to.RealAttack > 7 {
				Attack_to.RealAttack = Attack_to.RealAttack - 4
			}else {
				Attack_to.RealAttack = 3
			}
			if Attack_from.RealHealth < 0 {
				Attack_to, Attack_from = kill(Attack_to, Attack_from)
				Attack_to = levelUp(Attack_to)
				flag = 1
			}else{
				flag = 2
			}
		}else {
			Attack_from, Attack_to = kill(Attack_from, Attack_to)
			Attack_from = levelUp(Attack_from)
			flag = 0
		}
	}
	// Write the state back to the ledger

	Attack_from_bytes,_ := json.Marshal(Attack_from)

	err3 := stub.PutState(Attack_from.Id, Attack_from_bytes)
	if err3 != nil {
		return shim.Error(err.Error())
	}

	Attack_to_bytes,_ := json.Marshal(Attack_to)
	err4 := stub.PutState(Attack_to.Id, Attack_to_bytes)
	if err4 != nil {
		return shim.Error(err.Error())
	}

	var data [3]string
	switch flag {
	case 0:
		data[0] = "你击败了对手"
	case 1:
		data[0] = "你被反杀了"
	case 2:
		data[0] = "经过了一轮激烈对战"
	}
	data[1] = string(Attack_from_bytes)
	data[2] = string(Attack_to_bytes)
	databytes, _ := json.Marshal(data)

	return shim.Success(databytes)
}
//击杀之后的收益计算
func kill(Attack_win Role, Attack_lose Role)(winner Role, loser Role){
	Attack_lose.RealHealth = 0
	Attack_win.BasisHealth = Attack_win.BasisHealth + Attack_lose.BasisHealth
	Attack_win.BasisAttack = Attack_win.BasisAttack + Attack_lose.BasisAttack
	Attack_win.RealHealth = int(float64(Attack_win.BasisHealth)*float64(0.5)) + Attack_win.RealHealth
	if Attack_win.RealHealth > Attack_win.BasisHealth {
		Attack_win.RealHealth = Attack_win.BasisHealth
	}
	Attack_win.RealAttack = Attack_win.BasisAttack
	Attack_win.Money = Attack_win.Money + Attack_lose.Money
	Attack_win.Exp = Attack_win.Exp + Attack_lose.Exp
	return Attack_win,Attack_lose
}
//检查是否升级
func levelUp(role Role) (r Role) {
	flag := role.Level*role.Level*10
	if role.Exp >= flag {
		role.Level = role.Level+1
	}
	return role
}
//牛顿法开平方根
func Sqrt(x float64) float64 {
	z := 1.0
	for {
		tmp := z - (z*z-x)/(2*z)
		if tmp == z || math.Abs(tmp-z) < 0.000000000001 {
			break
		}
		z = tmp
	}
	return z
}

//删除
func (t *SimpleChaincode) delete(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}
	var id string
	var err error

	id = args[0]
	if err != nil {
		return shim.Error("Expecting integer value for asset holding")
	}

	// Delete the key from the state in ledger
	err = stub.DelState(id)
	if err != nil {
		return shim.Error("Failed to delete state")
	}

	return shim.Success(nil)
}

//查看
func (t *SimpleChaincode) query(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var id string
	var data [2]string

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting name of the person to query")
	}

	id = args[0]

	// Get the state from the ledger
	rolebytes, err := stub.GetState(id)
	if err != nil {
		jsonResp := "{\"Error\":\"Failed to get state for " + id + "\"}"
		return shim.Error(jsonResp)
	}

	if rolebytes == nil {
		jsonResp := "{\"Error\":\"Nil amount for " + id + "\"}"
		return shim.Error(jsonResp)
	}

	data[0] = "查询成功"
	data[1] = string(newRolebytes)
	databytes,_ := json.Marshal(data)

	return shim.Success(databytes)
}

//复活
func (t *SimpleChaincode) rebirth(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	role := Role{}
	var id string

	id = args[0]
	rolebytes, err := stub.GetState(id)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	if rolebytes == nil {
		return shim.Error("Entity not found")
	}
	err1 := json.Unmarshal(rolebytes, &role)

	
	role.RealHealth = role.BasisHealth
	role.RealAttack = role.BasisAttack
	role.Money = role.Money - 10

	newRolebytes, _ := json.Marshal(role)
	err2 := stub.PutState(role.Id, newRolebytes)
	if err2 != nil {
		return shim.Error(err.Error())
	}

	var data [2]string
	if role.Money < 10 {
		data[0] = "复活成功"
	}else {
		data[0] = "金币不足"
	}
	data[1] = string(newRolebytes)
	databytes,_ := json.Marshal(data)

	return shim.Success(databytes)
}

//治疗
func (t *SimpleChaincode) cure(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	Patient := Role{}
	Doctor := Role{}
	var id_p, id_d string
	var cureType int    //1:20金治疗50%  2:50金治疗100%
	var err error
	flag := false

	id_p = args[0]
	id_d = args[1]

	cureType, err = strconv.Atoi(args[2])
	if err != nil {
		return shim.Error("Expecting integer value for asset holding")
	}

	// Get the Attack_from's state from the ledger
	Pbytes, err1 := stub.GetState(id_p)
	if err1 != nil {
		return shim.Error("Failed to get state")
	}
	if Pbytes == nil {
		return shim.Error("Entity not found")
	}
	err = json.Unmarshal(Pbytes, &Patient)

	// Get the Attack_from's state from the ledger
	Dbytes, err2 := stub.GetState(id_d)
	if err2 != nil {
		return shim.Error("Failed to get state")
	}
	if Dbytes == nil {
		return shim.Error("Entity not found")
	}
	err = json.Unmarshal(Dbytes, &Doctor)

	switch cureType {
	case 1:
		Patient, Doctor, flag = cc(Patient, Doctor, 20)
	case 2:
		Patient, Doctor, flag = cc(Patient, Doctor, 50)
	}

	// Write the state back to the ledger
	Patient_bytes,_ := json.Marshal(Patient)
	err = stub.PutState(Patient.Id, Patient_bytes)
	if err != nil {
		return shim.Error(err.Error())
	}

	Doctor_bytes,_ := json.Marshal(Doctor)
	err = stub.PutState(Doctor.Id, Doctor_bytes)
	if err != nil {
		return shim.Error(err.Error())
	}

	var data [3]string

	if !flag {
		data[0] = "Don't have enough money"
	}else {
		data[0] = "修理成功"
	}
	data[1] = string(Patient_bytes)
	data[2] = string(Doctor_bytes)

	databytes,_ := json.Marshal(data)

	return shim.Success(databytes)
}

func cc(patient Role, doctor Role, money int)( Role,  Role,  bool){
	if patient.Money < money{
		return patient, doctor, false
	}else {
		patient.Money = patient.Money - money
		doctor.Money = doctor.Money + money
		if money == 20 {
			patient.RealHealth = int(float64(patient.RealHealth) + float64(patient.BasisHealth)*float64(0.5))
		}else if money == 50{
			patient.RealHealth = patient.RealHealth + patient.BasisHealth
		}
		if patient.RealHealth > patient.BasisHealth{
			patient.RealHealth = patient.BasisHealth
		}
		return patient, doctor, true
	}
}

//修补装备
func (t *SimpleChaincode) repair(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	Customer := Role{}
	Blacksmith := Role{}
	var id_c, id_b string
	var cureType int    //1:20金修复50%  2:50金修复100%
	flag := false
	var err error

	id_c = args[0]
	id_b = args[1]

	repairType, err1 := strconv.Atoi(args[2])
	if err1 != nil {
		return shim.Error("Expecting integer value for asset holding")
	}

	// Get the Attack_from's state from the ledger
	Cbytes, err2 := stub.GetState(id_c)
	if err2 != nil {
		return shim.Error("Failed to get state")
	}
	if Cbytes == nil {
		return shim.Error("Entity not found")
	}
	err = json.Unmarshal(Cbytes, &Customer)

	// Get the Attack_from's state from the ledger
	Bbytes, err3 := stub.GetState(id_b)
	if err3 != nil {
		return shim.Error("Failed to get state")
	}
	if Bbytes == nil {
		return shim.Error("Entity not found")
	}
	err = json.Unmarshal(Bbytes, &Blacksmith)

	switch repairType {
	case 1:
		Customer, Blacksmith, flag = rr(Customer, Blacksmith, 20)
	case 2:
		Customer, Blacksmith, flag = rr(Customer, Blacksmith, 50)
	}

	// Write the state back to the ledger
	Customer_bytes,_ := json.Marshal(Customer)
	err = stub.PutState(Customer.Id, Customer_bytes)
	if err != nil {
		return shim.Error(err.Error())
	}

	Blacksmith_bytes,_ := json.Marshal(Blacksmith)
	err = stub.PutState(Blacksmith.Id, Blacksmith_bytes)
	if err != nil {
		return shim.Error(err.Error())
	}

	var data [3]string

	if flag {
		data[0] = "金币不足"
	}else {
		data[0] = "修理成功"
	}
	data[1] = string(Customer_bytes)
	data[2] = string(Blacksmith_bytes)
	
	databytes,_ := json.Marshal(data)

	return shim.Success(databytes)
}
func rr(customer Role, blacksmith Role, money int)( Role, Role, bool){
	if customer.Money < money{
		return customer, blacksmith, false
	}else {
		customer.Money = customer.Money - money
		blacksmith.Money = blacksmith.Money + money
		if money == 20 {
			customer.RealAttack = int(float64(customer.RealAttack) + float64(customer.BasisAttack)*float64(0.5))
		}else if money == 50{
			customer.RealAttack = customer.RealAttack + customer.BasisAttack
		}
		if customer.RealAttack > customer.BasisAttack{
			customer.RealAttack = customer.BasisAttack
		}
		return customer, blacksmith, true
	}
}

func main() {
	err := shim.Start(new(SimpleChaincode))
	if err != nil {
		fmt.Printf("Error starting Simple chaincode: %s", err)
	}
}