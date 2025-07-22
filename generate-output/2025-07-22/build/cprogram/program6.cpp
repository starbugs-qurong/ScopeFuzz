#include <iostream> 
//insertion-include-available
using namespace std;
 
int OpW6150 = 0;
int Hrp969x[5] = {0,9,5,3,1};
//insertion-global-available



template <bool> struct sa;

template <> struct sa<true> {
};


template <class T, T v>
struct integral_constant
{

static const T                  value = v;

typedef T                       value_type;

typedef integral_constant<T, v> type;

};


typedef integral_constant<bool, true>  true_type;

typedef integral_constant<bool, false> false_type;


template <class T> struct is_lvalue_reference     : public integral_constant<bool, false> {
};

template <class T> struct is_lvalue_reference<T&> : public integral_constant<bool, true> {
};


template <class T> struct is_rvalue_reference      : public integral_constant<bool, false> {
};

template <class T> struct is_rvalue_reference<T&&> : public integral_constant<bool, true> {
};


template <class T> struct remove_reference      {
typedef T type;
};

template <class T> struct remove_reference<T&>  {
typedef T type;
};

template <class T> struct remove_reference<T&&> {
typedef T type;
};


template <class T> struct is_const          : public integral_constant<bool, false> {
};

template <class T> struct is_const<T const> : public integral_constant<bool, true> {
};


template <class T> struct is_volatile             : public integral_constant<bool, false> {
};

template <class T> struct is_volatile<T volatile> : public integral_constant<bool, true> {
};


struct A {
};


typedef A& Alref;

typedef const A& cAlref;

typedef volatile A& vAlref;

typedef const volatile A& cvAlref;


typedef A&& Arref;

typedef const A&& cArref;

typedef volatile A&& vArref;

typedef const volatile A&& cvArref;


template <class T, bool is_lvalue_ref, bool is_rvalue_ref, bool s_const, bool s_volatile>
void test()
{

sa<is_lvalue_reference<T>::value == is_lvalue_ref> t1;

sa<is_rvalue_reference<T>::value == is_rvalue_ref> t2;

sa<is_const   <typename remove_reference<T>::type>::value == s_const>    t3;

sa<is_volatile<typename remove_reference<T>::type>::value == s_volatile> t4;

sa<is_const   <typename remove_reference<const          T>::type>::value == s_const   > t5;

sa<is_volatile<typename remove_reference<      volatile T>::type>::value == s_volatile> t6;

}





template <bool> struct sa;

template <> struct sa<true> {
};


template <class T, T v>
struct integral_constant
{

static const T                  value = v;

typedef T                       value_type;

typedef integral_constant<T, v> type;

};


typedef integral_constant<bool, true>  true_type;

typedef integral_constant<bool, false> false_type;


template <class T> struct is_lvalue_reference     : public integral_constant<bool, false> {
};

template <class T> struct is_lvalue_reference<T&> : public integral_constant<bool, true> {
};


template <class T> struct is_rvalue_reference      : public integral_constant<bool, false> {
};

template <class T> struct is_rvalue_reference<T&&> : public integral_constant<bool, true> {
};


template <class T> struct remove_reference      {
typedef T type;
};

template <class T> struct remove_reference<T&>  {
typedef T type;
};

template <class T> struct remove_reference<T&&> {
typedef T type;
};


template <class T> struct is_const          : public integral_constant<bool, false> {
};

template <class T> struct is_const<T const> : public integral_constant<bool, true> {
};


template <class T> struct is_volatile             : public integral_constant<bool, false> {
};

template <class T> struct is_volatile<T volatile> : public integral_constant<bool, true> {
};


struct A {
};


typedef A& Alref;

typedef const A& cAlref;

typedef volatile A& vAlref;

typedef const volatile A& cvAlref;


typedef A&& Arref;

typedef const A&& cArref;

typedef volatile A&& vArref;

typedef const volatile A&& cvArref;


template <class T, bool is_lvalue_ref, bool is_rvalue_ref, bool s_const, bool s_volatile>
void test()
{

sa<is_lvalue_reference<T>::value == is_lvalue_ref> t1;

sa<is_rvalue_reference<T>::value == is_rvalue_ref> t2;

sa<is_const   <typename remove_reference<T>::type>::value == s_const>    t3;

sa<is_volatile<typename remove_reference<T>::type>::value == s_volatile> t4;

sa<is_const   <typename remove_reference<const          T>::type>::value == s_const   > t5;

sa<is_volatile<typename remove_reference<      volatile T>::type>::value == s_volatile> t6;

}


  long rD08IIJ = 8;
 short Jhe04H4n = 4;
  int c2H2613[10] = {6,1,6,5,6,1,4,7,6,9};
   template < typename T >  void vLx6acU ( int * y85e74bc ) {
 int e87U809 = ( OpW6150 - ( 3 ) ) ;
 int si92U2Oo[7] = {( OpW6150 - ( ( Jhe04H4n * ( Jhe04H4n ) ) ) ),5,5,7,1,2,5};
  rD08IIJ=((((rD08IIJ==0)?1:(rD08IIJ))==0)?1:(((rD08IIJ==0)?1:(rD08IIJ))))%((((rD08IIJ==0)?1:(rD08IIJ))==0)?1:(((rD08IIJ==0)?1:(rD08IIJ)))) ;
 cout << rD08IIJ<< endl;
cout << Jhe04H4n<< endl;
for(int i=0;
i<10;
i++){
cout << c2H2613[i]<< endl;
}
cout << OpW6150<< endl;
return;
 }
 template < typename T > class v8Y3X8y {
 public : };
 void KF49L8O (  ) {
//insertion-scope-first-available
//insertion-scope-first-statement-available
 int gw9rN013 = OpW6150 ;
 int Ybdl4zD[6] = {2,1,8,1,3,8};
 if ( (((((1)))+(Jhe04H4n)))==((+OpW6150)) ) {
//insertion-scope-first-available
//insertion-scope-first-statement-available
//insertion-scope-first-available
//insertion-scope-first-statement-available
 int E5V463eH[10] = {( - OpW6150 ),( Jhe04H4n * ( 4 ) ),3,2,1,4,7,1,9,7};
 long l10ivLlF = ( - rD08IIJ ) ;
 int K87Vz7wcount=0;
do {
//insertion-scope-first-available
//insertion-scope-first-statement-available
K87Vz7wcount=K87Vz7wcount+1;
if(K87Vz7wcount>1){
break;
}
//insertion-scope-first-available
//insertion-scope-first-statement-available
 int v3Rn65o = ( Jhe04H4n * ( ( ( 6 ) ) ) ) ;
 int YrE904h = ( - OpW6150 ) ;
   //insertion-scope-last-available
//insertion-scope-last-statement-available
//insertion-scope-last-available
//insertion-scope-last-statement-available
}
 while ( (OpW6150)||(OpW6150) ) ;
  //insertion-scope-last-available
//insertion-scope-last-statement-available
//insertion-scope-last-available
//insertion-scope-last-statement-available
}
 int jR5M744count=0;
while ( (rD08IIJ)<=((((((rD08IIJ==0)?1:(rD08IIJ))==0)?1:(((rD08IIJ==0)?1:(rD08IIJ))))/(((((rD08IIJ==0)?1:(rD08IIJ))==0)?1:(((rD08IIJ==0)?1:(rD08IIJ))))))) ) {
//insertion-scope-first-available
//insertion-scope-first-statement-available
jR5M744count=jR5M744count+1;
if(jR5M744count>1){
break;
}
//insertion-scope-first-available
//insertion-scope-first-statement-available
 int Uqy4K24 = ( OpW6150 * ( ( - OpW6150 ) ) ) ;
  int K402j9bcount=0;
do {
//insertion-scope-first-available
//insertion-scope-first-statement-available
K402j9bcount=K402j9bcount+1;
if(K402j9bcount>1){
break;
}
//insertion-scope-first-available
//insertion-scope-first-statement-available
  int yEDcWTw[8] = {( gw9rN013 * ( 2 ) ),( ( Jhe04H4n ) * ( Jhe04H4n ) ),0,3,5,1,7,1};
  int hU48nOGx = ( ( Jhe04H4n * ( Jhe04H4n ) ) ) ;
 //insertion-scope-last-available
//insertion-scope-last-statement-available
//insertion-scope-last-available
//insertion-scope-last-statement-available
}
 while ( (gw9rN013)||(!(((3))<=(((5)+(((((((rD08IIJ==0)?1:(rD08IIJ))==0)?1:(((rD08IIJ==0)?1:(rD08IIJ)))))%(((((rD08IIJ==0)?1:(rD08IIJ))==0)?1:(((rD08IIJ==0)?1:(rD08IIJ))))))))))) ) ;
 int I3N3Ys6[2] = { Hrp969x[0],( ( 4 ) - ( 2 ) )};
 //insertion-scope-last-available
//insertion-scope-last-statement-available
//insertion-scope-last-available
//insertion-scope-last-statement-available
}
 return;
 cout << rD08IIJ<< endl;
cout << Jhe04H4n<< endl;
for(int i=0;
i<10;
i++){
cout << c2H2613[i]<< endl;
}
cout << OpW6150<< endl;
for(int i=0;
i<5;
i++){
cout << Hrp969x[i]<< endl;
}
//insertion-scope-last-available
//insertion-scope-last-statement-available
}
 void uy2sTAK (  ) {
//insertion-scope-first-available
//insertion-scope-first-statement-available
//insertion-scope-first-available
//insertion-scope-first-statement-available
 int e5Mc35A5[3] = {( Jhe04H4n - ( ( ( 7 ) ) ) ),( ( 2 ) - ( 2 ) ),3};
  int w912918Ecount=0;
for ( ;
 OpW6150 ;
 ) {
//insertion-scope-first-available
//insertion-scope-first-statement-available
w912918Ecount=w912918Ecount+1;
if(w912918Ecount>1){
break;
}
//insertion-scope-first-available
//insertion-scope-first-statement-available
 int BSz9gCAc = ( ( ( 2 ) + ( 9 ) ) ) ;
 int K5k6vP8 = ( Hrp969x[2] ) ;
 Jhe04H4n=((((Jhe04H4n==0)?1:(Jhe04H4n))==0)?1:(((Jhe04H4n==0)?1:(Jhe04H4n))))/((((Jhe04H4n==0)?1:(Jhe04H4n))==0)?1:(((Jhe04H4n==0)?1:(Jhe04H4n)))) ;
 short U08R32d = ( 0 ) ;
 //insertion-scope-last-available
//insertion-scope-last-statement-available
//insertion-scope-last-available
//insertion-scope-last-statement-available
}
 OpW6150^=(((Jhe04H4n-(((4)))))-((-OpW6150))) ;
 cout << rD08IIJ<< endl;
cout << Jhe04H4n<< endl;
for(int i=0;
i<10;
i++){
cout << c2H2613[i]<< endl;
}
cout << OpW6150<< endl;
for(int i=0;
i<5;
i++){
cout << Hrp969x[i]<< endl;
}
return;
 //insertion-scope-last-available
//insertion-scope-last-statement-available
//insertion-scope-last-available
//insertion-scope-last-statement-available
}
 //insertion-global-available
int main() {
//insertion-scope-first-available
//insertion-scope-first-statement-available
//insertion-scope-first-available
//insertion-scope-first-statement-available
 //insertion-main-first-available
int *c50G0z8V = &OpW6150;
test<               A&,   true, false, false, false>();

test<const          A&,   true, false,  true, false>();

test<      volatile A&,   true, false, false,  true>();

test<const volatile A&,   true, false,  true,  true>();

test<               A&&, false,  true, false, false>();

test<const          A&&, false,  true,  true, false>();

test<      volatile A&&, false,  true, false,  true>();

test<const volatile A&&, false,  true,  true,  true>();



test<               Alref&,  true, false, false, false>();

test<const          Alref&,  true, false, false, false>();

test<      volatile Alref&,  true, false, false, false>();

test<const volatile Alref&,  true, false, false, false>();


test<               cAlref&,  true, false,  true, false>();

test<const          cAlref&,  true, false,  true, false>();

test<      volatile cAlref&,  true, false,  true, false>();

test<const volatile cAlref&,  true, false,  true, false>();


test<               vAlref&,  true, false, false,  true>();

test<const          vAlref&,  true, false, false,  true>();

test<      volatile vAlref&,  true, false, false,  true>();

test<const volatile vAlref&,  true, false, false,  true>();


test<               cvAlref&,  true, false,  true,  true>();

test<const          cvAlref&,  true, false,  true,  true>();

test<      volatile cvAlref&,  true, false,  true,  true>();

test<const volatile cvAlref&,  true, false,  true,  true>();


test<               Arref&,  true, false, false, false>();

test<const          Arref&,  true, false, false, false>();

test<      volatile Arref&,  true, false, false, false>();

test<const volatile Arref&,  true, false, false, false>();


test<               cArref&,  true, false,  true, false>();

test<const          cArref&,  true, false,  true, false>();

test<      volatile cArref&,  true, false,  true, false>();

test<const volatile cArref&,  true, false,  true, false>();


test<               vArref&,  true, false, false,  true>();

test<const          vArref&,  true, false, false,  true>();

test<      volatile vArref&,  true, false, false,  true>();

test<const volatile vArref&,  true, false, false,  true>();


test<               cvArref&,  true, false,  true,  true>();

test<const          cvArref&,  true, false,  true,  true>();

test<      volatile cvArref&,  true, false,  true,  true>();

test<const volatile cvArref&,  true, false,  true,  true>();



test<               Alref&&,  true, false, false, false>();

test<const          Alref&&,  true, false, false, false>();

test<      volatile Alref&&,  true, false, false, false>();

test<const volatile Alref&&,  true, false, false, false>();


test<               cAlref&&,  true, false,  true, false>();

test<const          cAlref&&,  true, false,  true, false>();

test<      volatile cAlref&&,  true, false,  true, false>();

test<const volatile cAlref&&,  true, false,  true, false>();


test<               vAlref&&,  true, false, false,  true>();

test<const          vAlref&&,  true, false, false,  true>();

test<      volatile vAlref&&,  true, false, false,  true>();

test<const volatile vAlref&&,  true, false, false,  true>();


test<               cvAlref&&,  true, false,  true,  true>();

test<const          cvAlref&&,  true, false,  true,  true>();

test<      volatile cvAlref&&,  true, false,  true,  true>();

test<const volatile cvAlref&&,  true, false,  true,  true>();


test<               Arref&&, false,  true, false, false>();

test<const          Arref&&, false,  true, false, false>();

test<      volatile Arref&&, false,  true, false, false>();

test<const volatile Arref&&, false,  true, false, false>();


test<               cArref&&, false,  true,  true, false>();

test<const          cArref&&, false,  true,  true, false>();

test<      volatile cArref&&, false,  true,  true, false>();

test<const volatile cArref&&, false,  true,  true, false>();


test<               vArref&&, false,  true, false,  true>();

test<const          vArref&&, false,  true, false,  true>();

test<      volatile vArref&&, false,  true, false,  true>();

test<const volatile vArref&&, false,  true, false,  true>();


test<               cvArref&&, false,  true,  true,  true>();

test<const          cvArref&&, false,  true,  true,  true>();

test<      volatile cvArref&&, false,  true,  true,  true>();

test<const volatile cvArref&&, false,  true,  true,  true>();


return 0;



v8Y3X8y<int> aS5O377v;
vLx6acU<int>(c50G0z8V);
KF49L8O();
uy2sTAK();
cout << rD08IIJ<< endl;
cout << Jhe04H4n<< endl;
for(int i=0;
i<10;
i++){
cout << c2H2613[i]<< endl;
}
cout << OpW6150<< endl;
for(int i=0;
i<5;
i++){
cout << Hrp969x[i]<< endl;
}
//insertion-main-last-available
test<               A&,   true, false, false, false>();

test<const          A&,   true, false,  true, false>();

test<      volatile A&,   true, false, false,  true>();

test<const volatile A&,   true, false,  true,  true>();

test<               A&&, false,  true, false, false>();

test<const          A&&, false,  true,  true, false>();

test<      volatile A&&, false,  true, false,  true>();

test<const volatile A&&, false,  true,  true,  true>();



test<               Alref&,  true, false, false, false>();

test<const          Alref&,  true, false, false, false>();

test<      volatile Alref&,  true, false, false, false>();

test<const volatile Alref&,  true, false, false, false>();


test<               cAlref&,  true, false,  true, false>();

test<const          cAlref&,  true, false,  true, false>();

test<      volatile cAlref&,  true, false,  true, false>();

test<const volatile cAlref&,  true, false,  true, false>();


test<               vAlref&,  true, false, false,  true>();

test<const          vAlref&,  true, false, false,  true>();

test<      volatile vAlref&,  true, false, false,  true>();

test<const volatile vAlref&,  true, false, false,  true>();


test<               cvAlref&,  true, false,  true,  true>();

test<const          cvAlref&,  true, false,  true,  true>();

test<      volatile cvAlref&,  true, false,  true,  true>();

test<const volatile cvAlref&,  true, false,  true,  true>();


test<               Arref&,  true, false, false, false>();

test<const          Arref&,  true, false, false, false>();

test<      volatile Arref&,  true, false, false, false>();

test<const volatile Arref&,  true, false, false, false>();


test<               cArref&,  true, false,  true, false>();

test<const          cArref&,  true, false,  true, false>();

test<      volatile cArref&,  true, false,  true, false>();

test<const volatile cArref&,  true, false,  true, false>();


test<               vArref&,  true, false, false,  true>();

test<const          vArref&,  true, false, false,  true>();

test<      volatile vArref&,  true, false, false,  true>();

test<const volatile vArref&,  true, false, false,  true>();


test<               cvArref&,  true, false,  true,  true>();

test<const          cvArref&,  true, false,  true,  true>();

test<      volatile cvArref&,  true, false,  true,  true>();

test<const volatile cvArref&,  true, false,  true,  true>();



test<               Alref&&,  true, false, false, false>();

test<const          Alref&&,  true, false, false, false>();

test<      volatile Alref&&,  true, false, false, false>();

test<const volatile Alref&&,  true, false, false, false>();


test<               cAlref&&,  true, false,  true, false>();

test<const          cAlref&&,  true, false,  true, false>();

test<      volatile cAlref&&,  true, false,  true, false>();

test<const volatile cAlref&&,  true, false,  true, false>();


test<               vAlref&&,  true, false, false,  true>();

test<const          vAlref&&,  true, false, false,  true>();

test<      volatile vAlref&&,  true, false, false,  true>();

test<const volatile vAlref&&,  true, false, false,  true>();


test<               cvAlref&&,  true, false,  true,  true>();

test<const          cvAlref&&,  true, false,  true,  true>();

test<      volatile cvAlref&&,  true, false,  true,  true>();

test<const volatile cvAlref&&,  true, false,  true,  true>();


test<               Arref&&, false,  true, false, false>();

test<const          Arref&&, false,  true, false, false>();

test<      volatile Arref&&, false,  true, false, false>();

test<const volatile Arref&&, false,  true, false, false>();


test<               cArref&&, false,  true,  true, false>();

test<const          cArref&&, false,  true,  true, false>();

test<      volatile cArref&&, false,  true,  true, false>();

test<const volatile cArref&&, false,  true,  true, false>();


test<               vArref&&, false,  true, false,  true>();

test<const          vArref&&, false,  true, false,  true>();

test<      volatile vArref&&, false,  true, false,  true>();

test<const volatile vArref&&, false,  true, false,  true>();


test<               cvArref&&, false,  true,  true,  true>();

test<const          cvArref&&, false,  true,  true,  true>();

test<      volatile cvArref&&, false,  true,  true,  true>();

test<const volatile cvArref&&, false,  true,  true,  true>();


return 0;



return 0;
 //insertion-scope-last-available
//insertion-scope-last-statement-available
//insertion-scope-last-available
//insertion-scope-last-statement-available
}
