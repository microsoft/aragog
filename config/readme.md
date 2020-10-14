# Writing and compiling invariants

The subfolders contain some examples of the invariant violation (IV) specification. Please refer to the paper section 5 of the paper to see how to write invariants. 

The generateSFA program compiles the IV specification to SFA, which is input to the runtime system.
After compilation, there are local and global SFAs generated. Global SFA files end with `<invariant_name>.g` and local SFA ends with `<invariant_name>.\d+` where `\d+` is an unsigned integer.

Some IV specification will not generate local SFA if the violation is not dependent on the location. Section 6.1 gives detail on how the local SFA is generated.

`aragog/config/debug/DEBUG_location.invar` is an example where local SFA is generated and some local events are suppressed from going to the global verifier.  
`aragog/config/firewall/*` do not have local SFA. Only filtering of events happen locally.