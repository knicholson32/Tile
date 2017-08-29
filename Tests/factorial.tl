function factorial(number val){

  if (val == 1){
    return 1;
  }else{
    ans = factorial(val-1);
    scope();
    sys_println("Ans: " + ans + " :: Val: " + val);
    return ans * val;
  }
}

a = factorial(4);
sys_println("" + a);
