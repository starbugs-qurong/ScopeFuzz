#include <iostream> 
//insertion-include-available
using namespace std;
 
short rS7Y1T7c = 3;
//insertion-global-available

template<typename _Tp>
inline _Tp&&
movel(_Tp& __t)
{
 return static_cast<_Tp&&>(__t);
 }


struct S{
};


S l;

S const cl = l;

S r() {
 return l;
 }

S const cr() {
 return l;
 }

S & nl = l;

S const & ncl = l;

S && nr = movel(l);

S const && ncr = movel(l);

S & ul() {
 return l;
 }

S const & ucl() {
 return l;
 }

S && ur() {
 return movel(l);
 }

S const && ucr() {
 return movel(l);
 }


void l0001(const S&&) {
}


void l0010(S&&) {
}


void l0011(S&&) {
}

void l0011(const S&&);


void l0100(const S&) {
}


void l0101(const S&) {
}

void l0101(const S&&);


void l0110(const S&) {
}

void l0110(S&&);


void l0111(const S&) {
}

void l0111(S&&);

void l0111(const S&&);


void l1000(S&) {
}


void l1001(S&) {
}

void l1001(const S&&);


void l1010(S&) {
}

void l1010(S&&);


void l1011(S&) {
}

void l1011(S&&);

void l1011(const S&&);


void l1100(S&) {
}

void l1100(const S&);


void l1101(S&) {
}

void l1101(const S&);

void l1101(const S&&);


void l1110(S&) {
}

void l1110(const S&);

void l1110(S&&);


void l1111(S&) {
}

void l1111(const S&);

void l1111(S&&);

void l1111(const S&&);


void cl0001(const S&&) {
}


void cl0011(S&&);

void cl0011(const S&&) {
}


void cl0100(const S&) {
}


void cl0101(const S&) {
}

void cl0101(const S&&);


void cl0110(const S&) {
}

void cl0110(S&&);


void cl0111(const S&) {
}

void cl0111(S&&);

void cl0111(const S&&);


void cl1001(S&);

void cl1001(const S&&) {
}


void cl1011(S&);

void cl1011(S&&);

void cl1011(const S&&) {
}


void cl1100(S&);

void cl1100(const S&) {
}


void cl1101(S&);

void cl1101(const S&) {
}

void cl1101(const S&&);


void cl1110(S&);

void cl1110(const S&) {
}

void cl1110(S&&);


void cl1111(S&);

void cl1111(const S&) {
}

void cl1111(S&&);

void cl1111(const S&&);


void r0001(const S&&) {
}


void r0010(S&&) {
}


void r0011(S&&) {
}

void r0011(const S&&);


void r0100(const S&) {
}


void r0101(const S&);

void r0101(const S&&) {
}


void r0110(const S&);

void r0110(S&&) {
}


void r0111(const S&);

void r0111(S&&) {
}

void r0111(const S&&);


void r1001(S&);

void r1001(const S&&) {
}


void r1010(S&);

void r1010(S&&) {
}


void r1011(S&);

void r1011(S&&) {
}

void r1011(const S&&);


void r1100(S&);

void r1100(const S&) {
}


void r1101(S&);

void r1101(const S&);

void r1101(const S&&) {
}


void r1110(S&);

void r1110(const S&);

void r1110(S&&) {
}


void r1111(S&);

void r1111(const S&);

void r1111(S&&) {
}

void r1111(const S&&);


void cr0001(const S&&) {
}


void cr0011(S&&);

void cr0011(const S&&) {
}


void cr0100(const S&) {
}


void cr0101(const S&);

void cr0101(const S&&) {
}


void cr0110(const S&) {
}

void cr0110(S&&);


void cr0111(const S&);

void cr0111(S&&);

void cr0111(const S&&) {
}


void cr1001(S&);

void cr1001(const S&&) {
}


void cr1011(S&);

void cr1011(S&&);

void cr1011(const S&&) {
}


void cr1100(S&);

void cr1100(const S&) {
}


void cr1101(S&);

void cr1101(const S&);

void cr1101(const S&&) {
}


void cr1110(S&);

void cr1110(const S&) {
}

void cr1110(S&&);


void cr1111(S&);

void cr1111(const S&);

void cr1111(S&&);

void cr1111(const S&&) {
}


void nl0001(const S&&) {
}


void nl0010(S&&) {
}


void nl0011(S&&) {
}

void nl0011(const S&&);


void nl0100(const S&) {
}


void nl0101(const S&) {
}

void nl0101(const S&&);


void nl0110(const S&) {
}

void nl0110(S&&);


void nl0111(const S&) {
}

void nl0111(S&&);

void nl0111(const S&&);


void nl1000(S&) {
}


void nl1001(S&) {
}

void nl1001(const S&&);


void nl1010(S&) {
}

void nl1010(S&&);


void nl1011(S&) {
}

void nl1011(S&&);

void nl1011(const S&&);


void nl1100(S&) {
}

void nl1100(const S&);


void nl1101(S&) {
}

void nl1101(const S&);

void nl1101(const S&&);


void nl1110(S&) {
}

void nl1110(const S&);

void nl1110(S&&);


void nl1111(S&) {
}

void nl1111(const S&);

void nl1111(S&&);

void nl1111(const S&&);


void ncl0001(const S&&) {
}


void ncl0011(S&&);

void ncl0011(const S&&) {
}


void ncl0100(const S&) {
}


void ncl0101(const S&) {
}

void ncl0101(const S&&);


void ncl0110(const S&) {
}

void ncl0110(S&&);


void ncl0111(const S&) {
}

void ncl0111(S&&);

void ncl0111(const S&&);


void ncl1001(S&);

void ncl1001(const S&&) {
}


void ncl1011(S&);

void ncl1011(S&&);

void ncl1011(const S&&) {
}


void ncl1100(S&);

void ncl1100(const S&) {
}


void ncl1101(S&);

void ncl1101(const S&) {
}

void ncl1101(const S&&);


void ncl1110(S&);

void ncl1110(const S&) {
}

void ncl1110(S&&);


void ncl1111(S&);

void ncl1111(const S&) {
}

void ncl1111(S&&);

void ncl1111(const S&&);


void nr0001(const S&&) {
}


void nr0010(S&&) {
}


void nr0011(S&&) {
}

void nr0011(const S&&);


void nr0100(const S&) {
}


void nr0101(const S&) {
}

void nr0101(const S&&);


void nr0110(const S&) {
}

void nr0110(S&&);


void nr0111(const S&) {
}

void nr0111(S&&);

void nr0111(const S&&);


void nr1000(S&) {
}


void nr1001(S&) {
}

void nr1001(const S&&);


void nr1010(S&) {
}

void nr1010(S&&);


void nr1011(S&) {
}

void nr1011(S&&);

void nr1011(const S&&);


void nr1100(S&) {
}

void nr1100(const S&);


void nr1101(S&) {
}

void nr1101(const S&);

void nr1101(const S&&);


void nr1110(S&) {
}

void nr1110(const S&);

void nr1110(S&&);


void nr1111(S&) {
}

void nr1111(const S&);

void nr1111(S&&);

void nr1111(const S&&);


void ncr0001(const S&&) {
}


void ncr0011(S&&);

void ncr0011(const S&&) {
}


void ncr0100(const S&) {
}


void ncr0101(const S&) {
}

void ncr0101(const S&&);


void ncr0110(const S&) {
}

void ncr0110(S&&);


void ncr0111(const S&) {
}

void ncr0111(S&&);

void ncr0111(const S&&);


void ncr1001(S&);

void ncr1001(const S&&) {
}


void ncr1011(S&);

void ncr1011(S&&);

void ncr1011(const S&&) {
}


void ncr1100(S&);

void ncr1100(const S&) {
}


void ncr1101(S&);

void ncr1101(const S&) {
}

void ncr1101(const S&&);


void ncr1110(S&);

void ncr1110(const S&) {
}

void ncr1110(S&&);


void ncr1111(S&);

void ncr1111(const S&) {
}

void ncr1111(S&&);

void ncr1111(const S&&);


void ul0001(const S&&) {
}


void ul0010(S&&) {
}


void ul0011(S&&) {
}

void ul0011(const S&&);


void ul0100(const S&) {
}


void ul0101(const S&) {
}

void ul0101(const S&&);


void ul0110(const S&) {
}

void ul0110(S&&);


void ul0111(const S&) {
}

void ul0111(S&&);

void ul0111(const S&&);


void ul1000(S&) {
}


void ul1001(S&) {
}

void ul1001(const S&&);


void ul1010(S&) {
}

void ul1010(S&&);


void ul1011(S&) {
}

void ul1011(S&&);

void ul1011(const S&&);


void ul1100(S&) {
}

void ul1100(const S&);


void ul1101(S&) {
}

void ul1101(const S&);

void ul1101(const S&&);


void ul1110(S&) {
}

void ul1110(const S&);

void ul1110(S&&);


void ul1111(S&) {
}

void ul1111(const S&);

void ul1111(S&&);

void ul1111(const S&&);


void ucl0001(const S&&) {
}


void ucl0011(S&&);

void ucl0011(const S&&) {
}


void ucl0100(const S&) {
}


void ucl0101(const S&) {
}

void ucl0101(const S&&);


void ucl0110(const S&) {
}

void ucl0110(S&&);


void ucl0111(const S&) {
}

void ucl0111(S&&);

void ucl0111(const S&&);


void ucl1001(S&);

void ucl1001(const S&&) {
}


void ucl1011(S&);

void ucl1011(S&&);

void ucl1011(const S&&) {
}


void ucl1100(S&);

void ucl1100(const S&) {
}


void ucl1101(S&);

void ucl1101(const S&) {
}

void ucl1101(const S&&);


void ucl1110(S&);

void ucl1110(const S&) {
}

void ucl1110(S&&);


void ucl1111(S&);

void ucl1111(const S&) {
}

void ucl1111(S&&);

void ucl1111(const S&&);


void ur0001(const S&&) {
}


void ur0010(S&&) {
}


void ur0011(S&&) {
}

void ur0011(const S&&);


void ur0100(const S&) {
}


void ur0101(const S&);

void ur0101(const S&&) {
}


void ur0110(const S&);

void ur0110(S&&) {
}


void ur0111(const S&);

void ur0111(S&&) {
}

void ur0111(const S&&);


void ur1001(S&);

void ur1001(const S&&) {
}


void ur1010(S&);

void ur1010(S&&) {
}


void ur1011(S&);

void ur1011(S&&) {
}

void ur1011(const S&&);


void ur1100(S&);

void ur1100(const S&) {
}


void ur1101(S&);

void ur1101(const S&);

void ur1101(const S&&) {
}


void ur1110(S&);

void ur1110(const S&);

void ur1110(S&&) {
}


void ur1111(S&);

void ur1111(const S&);

void ur1111(S&&) {
}

void ur1111(const S&&);


void ucr0001(const S&&) {
}


void ucr0011(S&&);

void ucr0011(const S&&) {
}


void ucr0100(const S&) {
}


void ucr0101(const S&);

void ucr0101(const S&&) {
}


void ucr0110(const S&) {
}

void ucr0110(S&&);


void ucr0111(const S&);

void ucr0111(S&&);

void ucr0111(const S&&) {
}


void ucr1001(S&);

void ucr1001(const S&&) {
}


void ucr1011(S&);

void ucr1011(S&&);

void ucr1011(const S&&) {
}


void ucr1100(S&);

void ucr1100(const S&) {
}


void ucr1101(S&);

void ucr1101(const S&);

void ucr1101(const S&&) {
}


void ucr1110(S&);

void ucr1110(const S&) {
}

void ucr1110(S&&);


void ucr1111(S&);

void ucr1111(const S&);

void ucr1111(S&&);

void ucr1111(const S&&) {
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


   long T3V10gkr = 0;
 int RJa82mf = 6;
 int i50y705[6] = {7,4,8,8,3,6};
 template <int> void f1() {
}
  template < typename T >  void qqXS0tp8 (  ) {
   RJa82mf|=(((0)&(0))&((((5)))==(8))) ;
 T3V10gkr^=5 ;
 cout << T3V10gkr<< endl;
cout << RJa82mf<< endl;
for(int i=0;
i<6;
i++){
cout << i50y705[i]<< endl;
}
return;
 }
  int Yg64q80 ( int O4GL6P74 ) {
//insertion-scope-first-available
//insertion-scope-first-statement-available
 int O7142Fz = ( RJa82mf * ( ( ( 7 ) + ( 9 ) ) ) ) ;
 int o3RY184O = ( - RJa82mf ) ;
 rS7Y1T7c^=rS7Y1T7c ;
  cout << O4GL6P74<< endl;
cout << T3V10gkr<< endl;
cout << RJa82mf<< endl;
for(int i=0;
i<6;
i++){
cout << i50y705[i]<< endl;
}
cout << rS7Y1T7c<< endl;
return ( ( ( rS7Y1T7c + ( 0 ) ) ) + ( ( 7 ) ) );
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
int M20l0M9 = 9;
l0100(l);

l0101(l);

l0110(l);

l0111(l);

l1000(l);

l1001(l);

l1010(l);

l1011(l);

l1100(l);

l1101(l);

l1110(l);

l1111(l);

cl0100(cl);

cl0101(cl);

cl0110(cl);

cl0111(cl);

cl1100(cl);

cl1101(cl);

cl1110(cl);

cl1111(cl);

r0001(r());

r0010(r());

r0011(r());

r0100(r());

r0101(r());

r0110(r());

r0111(r());

r1001(r());

r1010(r());

r1011(r());

r1100(r());

r1101(r());

r1110(r());

r1111(r());

cr0001(cr());

cr0011(cr());

cr0100(cr());

cr0101(cr());

cr0110(cr());

cr0111(cr());

cr1001(cr());

cr1011(cr());

cr1100(cr());

cr1101(cr());

cr1110(cr());

cr1111(cr());

nl0100(nl);

nl0101(nl);

nl0110(nl);

nl0111(nl);

nl1000(nl);

nl1001(nl);

nl1010(nl);

nl1011(nl);

nl1100(nl);

nl1101(nl);

nl1110(nl);

nl1111(nl);

ncl0100(ncl);

ncl0101(ncl);

ncl0110(ncl);

ncl0111(ncl);

ncl1100(ncl);

ncl1101(ncl);

ncl1110(ncl);

ncl1111(ncl);

nr0100(nr);

nr0101(nr);

nr0110(nr);

nr0111(nr);

nr1000(nr);

nr1001(nr);

nr1010(nr);

nr1011(nr);

nr1100(nr);

nr1101(nr);

nr1110(nr);

nr1111(nr);

ncr0100(ncr);

ncr0101(ncr);

ncr0110(ncr);

ncr0111(ncr);

ncr1100(ncr);

ncr1101(ncr);

ncr1110(ncr);

ncr1111(ncr);

ul0100(ul());

ul0101(ul());

ul0110(ul());

ul0111(ul());

ul1000(ul());

ul1001(ul());

ul1010(ul());

ul1011(ul());

ul1100(ul());

ul1101(ul());

ul1110(ul());

ul1111(ul());

ucl0100(ucl());

ucl0101(ucl());

ucl0110(ucl());

ucl0111(ucl());

ucl1100(ucl());

ucl1101(ucl());

ucl1110(ucl());

ucl1111(ucl());

ur0001(ur());

ur0010(ur());

ur0011(ur());

ur0100(ur());

ur0101(ur());

ur0110(ur());

ur0111(ur());

ur1001(ur());

ur1010(ur());

ur1011(ur());

ur1100(ur());

ur1101(ur());

ur1110(ur());

ur1111(ur());

ucr0001(ucr());

ucr0011(ucr());

ucr0100(ucr());

ucr0101(ucr());

ucr0110(ucr());

ucr0111(ucr());

ucr1001(ucr());

ucr1011(ucr());

ucr1100(ucr());

ucr1101(ucr());

ucr1110(ucr());

ucr1111(ucr());


return 0;



qqXS0tp8<int>();
int K5la741z=Yg64q80(M20l0M9);
cout << K5la741z<< endl;
cout << M20l0M9<< endl;
cout << T3V10gkr<< endl;
cout << RJa82mf<< endl;
for(int i=0;
i<6;
i++){
cout << i50y705[i]<< endl;
}
cout << rS7Y1T7c<< endl;
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
