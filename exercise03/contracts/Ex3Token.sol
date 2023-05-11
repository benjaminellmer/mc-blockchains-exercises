// SPDX-License-Identifier: MIT
pragma solidity ^0.8.9;

import "@openzeppelin/contracts/token/ERC20/ERC20.sol";

import "@openzeppelin/contracts/utils/math/SafeMath.sol";

contract Ex3Token is IERC20 {
    using SafeMath for Ex3Token;
    string public name;
    string public symbol;
    uint8 public decimals;
    uint256 public _totalSupply;

    constructor() {
        name = "EX3Token";
        symbol = "EX3";
        decimals = 3;
        _totalSupply = 2500000000; // 2.500.000,000

        // All tokens are sent to my address as owner
        balances[msg.sender] = _totalSupply;

        // Perform actual transfer by emitting Transfer () event
        emit Transfer(address(0), msg.sender, _totalSupply);
    }

    mapping(address => uint256) balances;
    mapping(address => mapping(address => uint256)) allowed;

    function allowance(address tokenOwner, address spender)
        public
        view
        returns (uint256 remaining)
    {
        return allowed[tokenOwner][spender];
    }

    function transfer(address to, uint256 tokens)
        public
        returns (bool success)
    {
        balances[msg.sender] = safeSub(balances[msg.sender], tokens);
        balances[to] = safeAdd(balances[to], tokens);
        emit Transfer(msg.sender, to, tokens);
        return true;
    }

    function approve(address spender, uint256 tokens)
        public
        returns (bool success)
    {
        allowed[msg.sender][spender] = tokens;
        emit Approval(msg.sender, spender, tokens);
        return true;
    }

    function totalSupply() public view returns (uint256) {
        return _totalSupply - balances[address(0)];
    }

    function transferFrom(
        address from,
        address to,
        uint256 tokens
    ) public returns (bool success) {
        balances[from] = safeSub(balances[from], tokens);
        allowed[from][msg.sender] = safeSub(allowed[from][msg.sender], tokens);
        balances[to] = safeAdd(balances[to], tokens);
        emit Transfer(from, to, tokens);
        return true;
    }

    function balanceOf(address tokenOwner)
        public
        view
        returns (uint256 balance)
    {
        return balances[tokenOwner];
    }

    function safeAdd(uint256 a, uint256 b) public pure returns (uint256 c) {
        c = a + b;
        require(c >= a);
    }

    function safeSub(uint256 a, uint256 b) public pure returns (uint256 c) {
        require(b <= a);
        c = a - b;
    }

    function safeMul(uint256 a, uint256 b) public pure returns (uint256 c) {
        c = a * b;
        require(a == 0 || c / a == b);
    }

    function safeDiv(uint256 a, uint256 b) public pure returns (uint256 c) {
        require(b > 0);
        c = a / b;
    }
}
