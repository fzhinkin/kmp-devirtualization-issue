Reproduces an issue with `CharSequence::get` calls not being devirtualized when the receiver's type is `String`. 

How to check if a problem exists:
- run `./gradlew build`
- inspect `./build/irDump/<target>/out.Codegen.ll`
- check bitcode emitted for `kfun:#wrapString(kotlin.String)`, `kfun:#wrapStringBuilder(kotlin.CharSequence)`, `kfun:#wrapStringWithTypeCheck(kotlin.String)` and `kfun:#wrapStringBuilderWithTypeCheck(kotlin.CharSequence)` functions: in all four cases, property getter is correctly devirtualized, but `get(Int)` function does not.

```
define i32 @"kfun:#wrapString(kotlin.String){}kotlin.Int"(%struct.ObjHeader* %0) #9 !dbg !12912 {
...
entry:                                            ; preds = %stack_locals_init
  store i32 0, i32* %sum, align 4, !dbg !12918
  store i32 0, i32* %inductionVariable, align 4, !dbg !12919
  %2 = call i32 @"kfun:kotlin.String#<get-length>(){}kotlin.Int"(%struct.ObjHeader* %0), !dbg !12920
  br label %call_success, !dbg !12920
...
do_while_loop:                                    ; preds = %loop_check, %when_case
  call void @Kotlin_mm_safePointWhileLoopBody() #13, !dbg !12921
  %5 = load i32, i32* %inductionVariable, align 4, !dbg !12919
  %6 = load i32, i32* %inductionVariable, align 4, !dbg !12919
  %7 = add i32 %6, 1, !dbg !12919
  store i32 %7, i32* %inductionVariable, align 4, !dbg !12919
  %8 = load i32, i32* %sum, align 4, !dbg !12922
  %9 = call zeroext i16 @"kfun:kotlin.CharSequence#get(kotlin.Int){}kotlin.Char-trampoline"(%struct.ObjHeader* %0, i32 %5), !dbg !12923
  br label %call_success1, !dbg !12923
...
```
